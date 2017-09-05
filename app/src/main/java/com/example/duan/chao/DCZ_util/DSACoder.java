package com.example.duan.chao.DCZ_util;

import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;

import org.apache.shiro.codec.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 常用数字签名算法DSA
 *
 * @author longshengtang
 */
public class DSACoder {

    //数字签名，密钥算法
    public static final String KEY_ALGORITHM = "DSA";
    /**
     * 数字签名
     * 签名/验证算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

    /**
     * DSA密钥长度，RSA算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到1024位之间
     */
    private static final int KEY_SIZE = 1024;
    //公钥
    private static final String PUBLIC_KEY = "DSAPublicKey";
    //私钥
    private static final String PRIVATE_KEY = "DSAPrivateKey";

 /*   public static void intkey() throws Exception {
        Map<String, Object> keyMap = initKey();// 构建密钥
        PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        //MyApplication.pub_key=publicKey.getFormat();MyApplication.sf.edit().putString("pub_key",publicKey.getFormat()).commit();
        PrivateKey privateKey = (PrivateKey) keyMap.get(PRIVATE_KEY);
        //  MyApplication.pri_key=privateKey.getFormat();MyApplication.sf.edit().putString("pri_key",privateKey.getFormat()).commit();
        String a = getPrivateKey(keyMap);
        MyApplication.pri_key=a;
        String b = getPublicKey(keyMap);
        MyApplication.pub_key=b;
        MyApplication.pub_key=b;MyApplication.sf.edit().putString("pub_key",b).commit();
        Log.i("dcz私钥format",a);
        Log.i("dcz公钥format",b);
        String str ="123";
        byte[] data = str.getBytes();
        String sign = DSA.sign(data,a);
        boolean verify1 = verify(str.getBytes(), getPublicKey(keyMap), sign);
        Log.i("经验证 数据和签名匹配:",verify1+"");
        Log.i("dcz_签名",sign);
    }*/



    /**
     * 签名
     *
     * @return String 数字签名Base64编码
     */
    public static String sign(String dataStr, String privateKeyBase64) throws Exception {
//        System.out.println("dataStr======"+dataStr);
        byte[] data = dataStr.getBytes();
        byte[] privateKey = Base64.decode(privateKeyBase64);
        byte[] sign = sign(data, privateKey);
        return Base64.encodeToString(sign);
    }

    /**
     * 签名
     *
     * @param privateKey 私钥字节数组
     * @return byte[] 数字签名
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        //初始化Signature
        signature.initSign(priKey);
        //更新
        signature.update(data);
        return signature.sign();
    }

    /**
     * 校验数字签名
     * //     * @param data 待校验数据
     * //     * @param publicKey 公钥Base64编码
     * //     * @param sign 数字签名Base64编码
     *
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(String dataStr, String publicKeyBase64, String signBase64) throws Exception {
        byte[] data = dataStr.getBytes();
        byte[] publicKey = Base64.decode(publicKeyBase64);
        byte[] sign = Base64.decode(signBase64);
        //验证
        return verify(data, publicKey, sign);
    }

    /**
     * 校验数字签名
     *
     * @param data      待校验数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        //转换公钥材料，实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥，密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        //初始化Signature
        signature.initVerify(pubKey);
        //更新
        signature.update(data);
        //验证
        return signature.verify(sign);
    }

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥
        DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();
        //甲方私钥
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 生成DSA秘钥对,经过Base64编码后的
     *
     * @return Map<String, String>
     * @throws Exception
     */
    public static Map<String, String> generateKeyPairs() throws Exception {
        //初始化密钥,生成密钥对
        Map<String, Object> keyMap = DSACoder.initKey();
        //公钥
        byte[] publicKey = DSACoder.getPublicKey(keyMap);
        String publicKeyBase64 = Base64.encodeToString(publicKey);
        //私钥
        byte[] privateKey = DSACoder.getPrivateKey(keyMap);
        String privateKeyBase64 = Base64.encodeToString(privateKey);

        Map<String, String> keyPairs = new HashMap<>();
        keyPairs.put(PUBLIC_KEY, publicKeyBase64);
        keyPairs.put(PRIVATE_KEY, privateKeyBase64);
        MyApplication.pub_key=publicKeyBase64;MyApplication.sf.edit().putString("pub_key",publicKeyBase64).commit();
        MyApplication.pri_key=privateKeyBase64;MyApplication.sf.edit().putString("pri_key",privateKeyBase64).commit();
        Log.i("公钥",publicKeyBase64);
        Log.i("私钥",privateKeyBase64);
       /* String str ="123456";
        byte[] data = str.getBytes();
        String sign = DSA.sign(data,MyApplication.pri_key);
        Log.i("sign",sign);*/
        return keyPairs;
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

/*    public static void intkey() throws Exception {
//        generateKeyPairs();
//        //初始化密钥
//        //生成密钥对
//        Map<String,Object> keyMap=DSACoder.initKey();
//        String str = "DSA数字签名算法";
        String str = "agreement=1&reqFlowId=2121&reqSysId=2001&srcReqSysId=1000&username=zml";
        str = "123456";
//        str = "agreement=1&reqFlowId=2121&reqSysId=2001&srcReqSysId=1000&username=zml";
        System.out.println("原文:"+str);
        String sign = null;
//
        String publicKey= "MIIBtzCCASsGByqGSM44BAEwggEeAoGBAMM2r4QPAh/h5CxUNh3wpicYNZ1sGLoz Nf3uZ7s4ew8jY/tThZheUVBJ2E3rhh4M2uQzflPqk/7ifw1woMF5afcZ2GUDi8Rt DShXiSKhMiYoPrMu/D+CWWvawEPZkuSZwBeaFWNg89ZMDx0r9dxXLDT1Owy/XlF7 8O5ETW1A3YbTAhUA2S85JGitqytm9XB6kxe7F6d1XtECgYAQ7QjqBIUHNKrOe+wW epdRzmut669dtcRu1rwp4eY+rd7g0QJfIfJ02fl7E6PeCfAk+diFWDhnb6NQtLvn P/NIOjGlCpHL8Ofnjv+B0c1t7cx4P09DWWwkn/ynfUJsTXxbSVp6B6z1owUDYdl8 O4V9TNb3Yo24oGTGhhIc5D+vEAOBhQACgYEAtXFArYEqCwBPdiH37OW+MSCva0pi RV9RPt/JMJIFZBawQyfukvfyq1Y8F5xBXArsptLpg99dyhVySTYuVwraL+9bniiX iDPHJ4tyjHUjlc8UTJKir1Yv00/tPEy+Ari9ASCOn2RLGF6rq+678jn7WNH6Mpi8 wn1D6Ji4lXkdAsc=";
        sign="MCwCFHI87TqPeSJ77JxfNb3Q0srnrb6LAhRvYxqiiwZJt1mhIWNthEJzgOGxBQ==";
        boolean status = DSACoder.verify(str, publicKey, sign);
        System.out.println("st===="+status);

        System.out.println(Base64.encodeToString("123456789".getBytes()));
    }*/
}