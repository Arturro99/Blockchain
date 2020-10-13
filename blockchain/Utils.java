package blockchain;

import java.io.*;
import java.util.List;

public class Utils {

    public static void serialize(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    public static boolean validateBlock(Block block) {
        String original = StringUtil.applySha256(block.getId() + block.getGeneratingTime()
                + block.getHashOfPreviousBlock() + block.getMessage() + block.getMagicNumber());

        return original.equals(block.getHash());
    }

    public static boolean validateChain(List<Block> chain) {
        return chain.stream().allMatch(block -> StringUtil.applySha256(block.getId() + block.getGeneratingTime()
                + block.getHashOfPreviousBlock() + block.getMessage() + block.getMagicNumber())
        .equals(block.getHash()));
    }

    public static Long countTransactions(List<Block> chain, String sender) {
        return chain.stream().flatMapToLong(block ->
                block.getMessage().stream().filter(message -> sender.equals(message.getSender())).mapToLong(Message::getAmount))
                .sum();
    }

    public static Long countBalance(List<Block> chain, String client) {
        return chain.stream().flatMapToLong(block ->
                block.getMessage().stream().filter(message -> client.equals(message.getReceiver())).mapToLong(Message::getAmount))
                .sum() - countTransactions(chain, client);
    }
}
