package com.example.duan.chao.DCZ_util;

/**
 * Created by DELL on 2017/8/11.
 */
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
  * DSA-Digital Signature Algorithm 是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(DigitalSignature Standard)。
  * 简单的说，这是一种更高级的验证方式，用作数字签名。不单单只有公钥、私钥，还有数字签名。私钥加密生成数字签名，公钥验证数据及签名。
  * 如果数据和签名不匹配则认为验证失败！即 传输中的数据 可以不再加密，接收方获得数据后，拿到公钥与签名 验证数据是否有效
  */
public class DSA {
 /**
  *不仅可以使用DSA算法，同样也可以使用RSA算法做数字签名
  */
  /*  public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";*/
    public static final String KEY_ALGORITHM = "DSA";
  //  public static final String SIGNATURE_ALGORITHM = "DSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";
    public static final String DEFAULT_SEED = "$%^*%^()(HJG8awfjas7"; //默认种子
    public static final String PUBLIC_KEY = "DSAPublicKey";
    public static final String PRIVATE_KEY = "DSAPrivateKey";

    public static void jdkDSA(String str) throws Exception {
      //  String str = "!@#$!#^$#&ZXVDF呆军工路爱着你*()_+";
        byte[] data = str.getBytes();
        Map<String, Object> keyMap = initKey();// 构建密钥
        PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        PrivateKey privateKey = (PrivateKey) keyMap.get(PRIVATE_KEY);
        Log.i("dcz私钥format",privateKey.getFormat());
        Log.i("dcz公钥format",publicKey.getFormat());
        // 产生签名
        String sign = sign(data, getPrivateKey(keyMap));
        // 验证签名
        boolean verify1 = verify("aaa".getBytes(), getPublicKey(keyMap), sign);
        Log.i("经验证 数据和签名匹配:",verify1+"");
        boolean verify = verify(data, getPublicKey(keyMap), sign);
        Log.i("经验证 数据和签名匹配:",verify+"");
    }

    public static void intkey() throws Exception {
        Map<String, Object> keyMap = initKey();// 构建密钥
       /* PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        MyApplication.pub_key=publicKey.getFormat();MyApplication.sf.edit().putString("pub_key",publicKey.getFormat()).commit();
        PrivateKey privateKey = (PrivateKey) keyMap.get(PRIVATE_KEY);
        MyApplication.pri_key=privateKey.getFormat();MyApplication.sf.edit().putString("pri_key",privateKey.getFormat()).commit();*/
        String a = getPrivateKey(keyMap);
        MyApplication.pri_key=a;MyApplication.sf.edit().putString("pri_key",a);
        String b = getPublicKey(keyMap);
        MyApplication.pub_key=b;MyApplication.sf.edit().putString("pub_key",b);
        Log.i("dcz私钥format",a);
        Log.i("dcz公钥format",b);
        String str ="123";
        byte[] data = str.getBytes();
        String sign = DSA.sign(data,a);
        boolean verify1 = verify(str.getBytes(), getPublicKey(keyMap), sign);
        Log.i("经验证 数据和签名匹配:",verify1+"");
        Log.i("dcz_签名",sign);
    }
    /**
    * 生成密钥
    *
    * @param seed 种子
    * @return 密钥对象
    * @throws Exception
    */
    public static Map<String, Object> initKey(String seed) throws Exception {
        System.out.println("生成密钥");
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed.getBytes());
        //Modulus size must range from 512 to 1024 and be a multiple of 64
        keygen.initialize(1024, secureRandom);
        KeyPair keys = keygen.genKeyPair();
        PrivateKey privateKey = keys.getPrivate();
        PublicKey publicKey = keys.getPublic();
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }

    /**
    * 生成默认密钥
    *
    * @return 密钥对象
    * @throws Exception
    */
    public static Map<String, Object> initKey() throws Exception {
        return initKey(DEFAULT_SEED);
    }
    /**
    * 取得私钥
    *
    * @param keyMap
    * @return
    * @throws Exception
    */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded()); //base64加密私钥
    }
    /**
    * 取得公钥
    *
    * @param keyMap
    * @return
    * @throws Exception
    */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded()); //base64加密公钥
    }
    /**
    * 用私钥对信息进行数字签名
    * @param data 加密数据
    * @param privateKey 私钥-base64加密的
    * @return
    * @throws Exception
    */
    public static String sign(byte[] data, String privateKey) throws Exception {
        Log.i("dcz","用私钥对信息进行数字签名");
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = factory.generatePrivate(keySpec);//生成 私钥
        // 用私钥对信息进行数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }
    /**
    * BASE64Encoder 加密
    * @param data 要加密的数据
    * @return 加密后的字符串
    */
    private static String encryptBASE64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(data);
        return encode;
    }
    /**
    * BASE64Decoder 解密
    * @param data 要解密的字符串
    * @return 解密后的byte[]
    * @throws Exception
    */
    private static byte[] decryptBASE64(String data) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer = decoder.decodeBuffer(data);
        return buffer;
    }
    /**
    * 校验数字签名
    * @param data 加密数据
    * @param publicKey
    * @param sign 数字签名
    * @return
    * @throws Exception
    */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(decryptBASE64(sign)); //验证签名
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //将 BASE64 编码的字符串 s 进行解码
    public void getBase64() {
        String s = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIURzpbXinUx4Naitrwd3YkbzFp6Wo=";
        String decodedString =new String(Base64.decode(s,Base64.DEFAULT));
        Log.e("Base64", "Base64---->" + decodedString);
    }
}