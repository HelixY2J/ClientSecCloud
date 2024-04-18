import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class encryption {

    public static List<Object> encrypt(String plainText, int keyValue,String bucketName) throws IOException {
        List<Object> result = new ArrayList<>();
        StringBuilder cipherText = new StringBuilder();
        StringBuilder encText = new StringBuilder();         
        StringBuilder encryptionKey = new StringBuilder();

        for (char character : plainText.toCharArray()) {
            // Get ASCII value, convert to binary, and handle leading zeros
            String binaryChar = Integer.toBinaryString(character).toString();
            binaryChar = String.format("%8s", binaryChar).replace(' ', '0');

            // Swap 4-bit chunks
            String swapped4Bit = binaryChar.substring(4) + binaryChar.substring(0, 4);

            // Swap 2-bit chunks within each 4-bit chunk
            String swapped2Bit = swapped4Bit.substring(2, 4) + swapped4Bit.substring(0, 2) +
                                swapped4Bit.substring(6, 8) + swapped4Bit.substring(4, 6);

            // Reverse the entire 8-bit value
            String reversedBinary = new StringBuilder(swapped2Bit).reverse().toString();

            
            // char encryptedChar = (char) (Integer.parseInt(reversedBinary, 2) + keyValue);
            char encryptedChar = (char) (Integer.parseInt(reversedBinary, 2));  // 129
            char encKeyChar = (char) (Integer.parseInt(reversedBinary, 2) + keyValue);  // Adding keys == 129 + keys 
            cipherText.append(encryptedChar);
            encText.append(encKeyChar);
           
            encryptionKey.append(keyValue); 
        }

        String filename = "./encrypted_data.txt";
        String file = "./new_enc.txt"; 
        try(FileWriter writer = new FileWriter(filename);BufferedWriter bw = new BufferedWriter(writer)){
            bw.write(bucketName.toString());
            bw.newLine();
            bw.write(encText.toString());
            bw.newLine();
            bw.write(encryptionKey.toString());
            bw.newLine();
            
            

        }
        try(FileWriter writer = new FileWriter(file)){
            writer.write(encryptionKey.toString() + "\n");
            writer.write(cipherText.toString() + "\n");    // saving 132 enc text
         }

        result.add(cipherText.toString());  
        result.add(encText.toString());    // adding 132 in arrayList
        
        result.add(encryptionKey.toString());
        return result;
    }
    public static int getKey(String bucketName){
        // String bucketName = "Amazonbucket" ;
        int value = 0;
        for (char character : bucketName.toCharArray()) {
            value += (int) character;
        }

        int keyValue = value % bucketName.length();
       // System.out.println("so they key is:" + keyValue);
        return keyValue;
    }

    public static void main(String[] args) {
    

        Scanner scanner = new Scanner(System.in);

        System.out.print("enter plaintext:");
        String plainText = scanner.nextLine();
        
        //scanner.close();
        System.out.print("enter bucket name:");
       // Scanner scanner2  = new Scanner(System.in);
        String bucket = scanner.nextLine();
        scanner.close();
        
        int keyValue = getKey(bucket);

        try {
            //String plainText = "The cargo leaves today with colonel sem";
            //int keyValue = 42;
    
            List<Object> encryptionResult = encrypt(plainText, keyValue,bucket);
            System.out.println("plain text: " + plainText);

            


            System.out.println("Cipher text: " + encryptionResult.get(0));
            
            
            System.out.println("Encrypted text: " + encryptionResult.get(1));
            System.out.println("Encryption key: " + encryptionResult.get(2));
          
            
        } catch(IOException e){
            System.err.println("Error enc and writing to file:" + e.getMessage());
        }
    }
}

