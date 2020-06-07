package UDP_Agent;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class PDU_Data extends PDU{

    // Variaveis de instancia e contutores

    private int seq;
    private int nr_bytes;
    private SecretKey key;
    private byte[] data;

    public PDU_Data(byte type, int client, String ip, int seq, int nr_bytes, SecretKey key, byte[] data) throws Exception{
        super(type,client,ip);
        this.seq = seq;
        this.nr_bytes = nr_bytes;
        this.key = key;
        this.data = data;
    }

    public byte[] pdu_toBytes(){
        byte[] base_pdu = super.pdu_toBytes();
        byte[] seq = ByteBuffer.allocate(4).putInt(this.getSeq()).array();
        byte[] nr_bytes = ByteBuffer.allocate(4).putInt(this.getNr_bytes()).array();
        byte[] key = ByteBuffer.allocate(16).put(this.getKey().getEncoded()).array();
        byte[] data = this.getData();
        byte[] result = new byte[base_pdu.length + seq.length + nr_bytes.length + key.length + data.length];
        System.arraycopy(base_pdu,0,result,0,base_pdu.length);
        System.arraycopy(seq,0,result,13,seq.length);
        System.arraycopy(nr_bytes,0,result,17,nr_bytes.length);
        System.arraycopy(key,0,result,21,key.length);
        System.arraycopy(data,0,result,37,data.length);
        return result;
    }

    // funcionalidades

    public void encryptAES(){
        byte[] encrypted_data = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,this.getKey());
            encrypted_data = cipher.doFinal(this.getData());
            this.data = new byte[encrypted_data.length];
            this.setData(encrypted_data);
            this.setNr_bytes(encrypted_data.length);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void decryptAES(){
        byte[] decrypted_data = null;
        try {
            this.setData(Arrays.copyOfRange(this.getData(),0,this.getNr_bytes()));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,this.getKey());
            decrypted_data = cipher.doFinal(this.getData());
            this.setData(decrypted_data);
            this.setNr_bytes(decrypted_data.length);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // get's e set's

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getNr_bytes() {
        return nr_bytes;
    }

    public void setNr_bytes(int nr_bytes) {
        this.nr_bytes = nr_bytes;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    ////////////

    @Override
    public String toString() {
        return "PDU_Data{" +
                "seq=" + seq
                ;
    }

}
