package blockchain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static byte[] privateKey, publicKey;
    private static int prefix = 0;
    private static volatile List<Block> blockChain;
    private static List<Message> chat = new ArrayList<>();

    private static List<String> clients = List.of("John", "Jack", "Jessica", "Ana");

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, ExecutionException, InterruptedException {

//        if(Files.exists(Path.of("chain.dat"))) {
//            blockChain = (List<Block>) Utils.deserialize("block.dat");
//            return;
//        }

        SecuredMessage keys = new SecuredMessage(1024);
        keys.createKeys();
        privateKey = keys.getPrivateKey().getEncoded();
        publicKey = keys.getPublicKey().getEncoded();

        blockChain = new ArrayList<>();
        start();

        for(Block b : blockChain)
            System.out.println(b);
    }

    public static void start() throws ExecutionException, InterruptedException, IOException {
        ExecutorService minerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService messagesExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for(int i = 1; i <= 15; i++) {
            long timeStamp = new Date().getTime();
            String hashOfThePreviousBlock;

            if(i == 1)
                hashOfThePreviousBlock = "0";
            else
                hashOfThePreviousBlock = blockChain.get(i - 2).getHash();

            Collection blockCalls = new ArrayList<>();
            Collection messagesCalls = new ArrayList<>();

            for(int j = 1; j < Runtime.getRuntime().availableProcessors(); j++)
                blockCalls.add(new BlockChain(i, timeStamp, hashOfThePreviousBlock, chat, prefix, privateKey, 100));

            clients.forEach(client -> messagesCalls.add(new Message(10, "", client, privateKey)));

            Object tmpFirstMinedBlock = minerExecutor.invokeAny(blockCalls);
            List tmpMessages = messagesExecutor.invokeAll(messagesCalls);

            Block newBlock = (Block)tmpFirstMinedBlock;
            newBlock.setId(i);

//            if(newBlock.getGeneratingTime() / 1000.0 > 5)
//                prefix--;
//            else if(newBlock.getGeneratingTime() / 1000.0 < 5)
//                prefix++;

            newBlock.adjustN(newBlock, prefix);
            blockChain.add(newBlock);
        }

        Utils.serialize(blockChain, "chain.dat");
        minerExecutor.shutdown();
        messagesExecutor.shutdown();
    }
}
