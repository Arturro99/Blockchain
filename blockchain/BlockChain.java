package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class BlockChain implements Callable {

    private final int id;
    private final long generatingTime;
    private final String hashOfThePreviousBlock;
    private final List<Message> message;
    private int magicNumber;
    private final int prefix;
    byte[] privateKey;
    long reward;

    public BlockChain(int id, long timestamp, String hashOfThePreviousBlock, List<Message> message, int prefix,
                            byte[] privateKey, long reward) {
        this.id = id;
        this.generatingTime = timestamp;
        this.hashOfThePreviousBlock = hashOfThePreviousBlock;
        this.message = new ArrayList<>();
        this.message.addAll(message);
        this.prefix = prefix;
        this.privateKey = privateKey;
        this.reward = reward;
    }

    @Override
    public Object call() throws Exception {

        message.add((Message) new Message(100L, "Transactions: \nBlockchain", "miner " + Thread.currentThread().getId(), privateKey).call());

        String hash = mineBlock(prefix);

        return new Block((int)Thread.currentThread().getId(), id, generatingTime, magicNumber, hashOfThePreviousBlock, hash, new Date().getTime() - generatingTime, message);
    }

    Random random = new Random();

    public String mineBlock(int prefix) {
        String prefixString = "0".repeat(prefix);
        String hash;
        do {
            magicNumber = random.nextInt(Integer.MAX_VALUE);
            hash = StringUtil.applySha256(id + generatingTime + hashOfThePreviousBlock + message + magicNumber);
        } while (!hash.subSequence(0, prefix).equals(prefixString));

        return hash;
    }
}