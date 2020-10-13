package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int minerId;
    private long timestamp;
    private long magicNumber;
    private String hashOfPreviousBlock;
    private String hash;
    private long generatingTime;
    private List<Message> message;

    private String numberOfZeros;


    Block(int id, int minerId, long timestamp, long magicNumber, String hashOfPreviousBlock,
          String hash, long generatingTime, List<Message> message) {
        this.id = id;
        this.minerId = minerId;
        this.timestamp = timestamp;
        this.magicNumber = magicNumber;
        this.hashOfPreviousBlock = hashOfPreviousBlock;
        this.hash = hash;
        this.generatingTime = generatingTime;
        this.message = message;
    }

    public void adjustN(Block block, int prefix) {
        int prevN = 0;
        for (int l = 0; l < block.getHash().length(); l++) {
            if (block.getHash().charAt(l) == '0') {
                prevN++;
            } else {
                break;
            }
        }

        if (prefix > prevN) {
            this.numberOfZeros = "N was increased to " + prefix + "\n";
        } else if (prefix < prevN) {
            this.numberOfZeros = "N was decreased by " + prefix + "\n";
        } else {
            this.numberOfZeros = "N stays the same" + "\n";

        }
    }

    public String viewTransactions() {
        if (message == null) return "no transactions";
        StringBuilder builder = new StringBuilder();
        for(Message s : message)
            builder.append(s.getMessage()).append("\n");
        return builder.toString();
    }


    public int getId() {
        return id;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public String getHashOfPreviousBlock() {
        return hashOfPreviousBlock;
    }

    public String getHash() {
        return hash;
    }

    public long getGeneratingTime() {
        return generatingTime;
    }

    public List<Message> getMessage() {
        return message;
    }

    public String getNumberOfZeros() {
        return numberOfZeros;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("\nBlock: "+
                "\nCreated by miner #" + minerId +
                minerId + "\ngets 100 VC" +
                "\nId: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block: \n" + hashOfPreviousBlock +
                "\nHash of the block: \n" + hash +
                "\nBlock data: \n" + viewTransactions() +
                "\nBlock was generating for " + generatingTime / 1000+ " seconds" +
                "\n" + numberOfZeros);
        return builder.toString();
    }

}
