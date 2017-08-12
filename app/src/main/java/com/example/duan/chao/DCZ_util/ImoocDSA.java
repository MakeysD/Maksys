package com.example.duan.chao.DCZ_util;

import android.util.Log;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by DELL on 2017/8/11.
 */

public class ImoocDSA {
    private static String src = "imooc security dsa";
    public static void jdkDSA() {
        try{
         //1.初始化密钥  
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("DSA");
            KeyPair keyPair =keyPairGenerator.generateKeyPair();
            DSAPrivateKey dsaPrivateKey=(DSAPrivateKey)keyPair.getPrivate();
            DSAPublicKey dsaPublicKey = (DSAPublicKey)keyPair.getPublic();
         //2.执行签名(dsaPrivateKey)  
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(dsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
         //签名  
            Signature signature=Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] result=signature.sign();
            Log.i("dcz",result+"结果");
         //3.验证签名(dsaPublicKey)  
            X509EncodedKeySpec x509EncodedKeySpec =new X509EncodedKeySpec(dsaPublicKey.getEncoded());
//          keyFactory = KeyFactory.getInstance("DSA");  
            PublicKey publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean b=signature.verify(result);
            System.out.println("jdk dsa verify:"+b);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
