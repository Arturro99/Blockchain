package blockchain;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Message implements Callable, Serializable {
    private static long serialVersionUID = 1L;

    private long amount;
    private String sender;
    private String receiver;
    private StringBuilder message;
    private List<byte[]> signature;
    private byte[] privateKey;

    public Message(long amount, String sender, String receiver,
                   byte[] privateKey) {
        this.amount = amount;
        this.privateKey = privateKey;
        this.sender = sender;
        this.receiver = receiver;
        this.message = new StringBuilder(" sent" + amount + "VC to" + receiver);
        this.signature = new ArrayList<>();
    }

    @Override
    public Object call() throws Exception {
        if ("".equals(this.sender))
            this.sender = "miner" + Thread.currentThread().getId();

        message.insert(0, sender);
        signature.add(message.toString().getBytes());
        signature.add(sign(message.toString(), privateKey));
        return this;
    }

    //The method that signs the data using the private key that is stored in keyFile path
    public byte[] sign(String data, byte[] privateKeyBytes) throws InvalidKeyException, Exception {
        Signature dsa = Signature.getInstance("SHA1withRSA");
        dsa.initSign(getPrivate(privateKeyBytes));
        dsa.update(data.getBytes());
        return dsa.sign();
    }

    //Method to retrieve the Private Key from a file
    public PrivateKey getPrivate(byte[] privateKeyBytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    public long getAmount() {
        return amount;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public StringBuilder getMessage() {
        return message;
    }

    public List<byte[]> getSignature() {
        return signature;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }
}
