package com.sdca.api.util;

import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneSVS;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequence;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class SignUtil {

    private static PCSClient pcsClient;
    private static SVSClient svs;

    public  static  String sign(String sourceData,String host,
                                String pcsport,String certID,String certPSW) throws NetonejExcepption, UnsupportedEncodingException {
        pcsClient = new PCSClient(host, pcsport);
        //String sourceData = JSON.toJSONString(sourceMap);
        //System.out.println(sourceData);
        String p1signValue = "";

        BASE64Encoder encoder = new BASE64Encoder();
        String base64SourceData = encoder.encode(sourceData.getBytes("UTF8"));//采用UTF8编码，与客户端一致。
        NetonePCS pcsReq = pcsClient.createPKCS1Signature(certID, certPSW, "kid", base64SourceData, "0", "ecdsa-sm2-with-sm3");
        if (pcsReq.getStatusCode() != 200) {
            System.out.println("错误码为：" + pcsReq.getStatusCode() + "/n签名结果：" + pcsReq.getStatusCodeMessage());
            return pcsReq.getStatusCode() + "";
        } else {
            //System.out.println("签名成功");
            //获取签名值，得到原文，比对是否为服务器生成的随机数
            p1signValue = pcsReq.getRetBase64String().toString();
           // System.out.println("P1签名信息为-->" + p1signValue);
            return p1signValue;
        }

    }


    public static boolean verifySignatureWithSVSToIsignature(String data, String signValue,
                                                             String pkc, String algo,
                                                             String svsIp, String svsPort) {

        svs = new SVSClient(svsIp, svsPort);

        String errorMsg = "";
        String SM2P1SignData = "";
        byte[] byteR = new byte[32];
        byte[] byteS = new byte[32];

        if(data.length() % 88 == 0){
            BASE64Decoder mydecoder = new BASE64Decoder();
            byte[] byteSignData;
            try {
                byteSignData = mydecoder.decodeBuffer(data);

                System.arraycopy(byteSignData, 0, byteR, 0, 32);
                System.arraycopy(byteSignData, 32, byteS, 0, 32);
            } catch (IOException e1) {

                return false;
            }

            BigInteger bigR;
            BigInteger bigS;

            if((byteR[0] & 0xff) >= 0x80){
                byte[] byteRR = new byte[33];
                System.arraycopy(byteR, 0, byteRR, 1, 32);
                bigR = new BigInteger(byteRR);
            }
            else
                bigR = new BigInteger(byteR);

            if((byteS[0] & 0xff) >= 0x80){
                byte[] byteSS = new byte[33];
                System.arraycopy(byteS, 0, byteSS, 1, 32);
                bigS = new BigInteger(byteSS);
            }
            else
                bigS = new BigInteger(byteS);
            ASN1EncodableVector localASN1EncodableVectorsm2 = new ASN1EncodableVector();
            localASN1EncodableVectorsm2.add(new ASN1Integer(bigR));
            localASN1EncodableVectorsm2.add(new ASN1Integer(bigS));

            try{
                BASE64Encoder encoder=new BASE64Encoder();
                SM2P1SignData = encoder.encode(new DERSequence(localASN1EncodableVectorsm2).getEncoded());

                data = SM2P1SignData;
            } catch (IOException e) {

                return false;
            }
        }else
        {
            //System.out.println("=====================验证签名===========================");
        }

        try {
            NetoneSVS result = svs.verifyPKCS1(data, signValue, "ecdsa-sm2-with-sm3", "0", pkc, false);
            int statusCode = result.getStatusCode();
            String msg = result.getStatusCodeMessage();
            if(result.getStatusCode() == 200) {

            } else {

                return false;
            }
        } catch (NetonejExcepption e) {

            if(errorMsg==null || errorMsg.isEmpty()) {
                errorMsg = "验证签名异常.";
                return false;
            }
        } catch (Exception e) {

            if(errorMsg==null || errorMsg.isEmpty()) {
                errorMsg = "验证签名异常.";
                return false;
            }
        } finally {

        }

        return true;
    }
}
