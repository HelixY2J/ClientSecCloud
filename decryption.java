package r;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class decryption {

    public static String decrypt(String cipherText, String encryptionKey,String encString) {
        StringBuilder plainText = new StringBuilder();

        // Iterate through characters of cipher text and encryption key
        for (int i = 0; i < cipherText.length(); i++) {
            char cipherChar = cipherText.charAt(i);
            char keyChar = encryptionKey.charAt(i);  
            char encChar = encString.charAt(i);

          

           int calculatedAscii = encChar - keyChar;
            if (calculatedAscii != (int) cipherChar) {
                System.out.println("Cipher text and key mismatch");
                 // Indicate failure
            }
            
            String binaryChar = Integer.toBinaryString(cipherChar);
            binaryChar = String.format("%8s", binaryChar).replace(' ', '0'); // Ensure 8 bits

            // Reverse, swap 2-bit chunks, swap 4-bit chunks
            String reversed4Bit = new StringBuilder(binaryChar).reverse().toString();
            String swapped2Bit = reversed4Bit.substring(2, 4) + reversed4Bit.substring(0, 2) +
                                 reversed4Bit.substring(6, 8) + reversed4Bit.substring(4, 6);
            String swapped4Bit = swapped2Bit.substring(4) + swapped2Bit.substring(0, 4);

            // Convert back to ASCII and append to plain text
            int decryptedAscii = Integer.parseInt(swapped4Bit, 2);
            char plainChar = (char) decryptedAscii;
            plainText.append(plainChar);
        }

        return plainText.toString();
    }

    public static class filecontent {
        String bucket;
        String encrypted_txt;

        public filecontent(String bucket,String encrypted_txt){
            this.bucket  = bucket;
            this.encrypted_txt = encrypted_txt;
        }
    }


    public static String readSecondFile(String file) {         // added a second readr to read from second file
        try (FileReader reader = new FileReader(file)) {
            Scanner scan = new Scanner(reader);
            String bucketName = scan.nextLine();
            String encT = scan.nextLine();
            scan.close();
            return encT;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        } 
    }

    public static void main(String[] args) {

	Scanner scanner = new Scanner(System.in);

    System.out.print("Enter file name containing cipher text and key (default: new_enc.txt): ");
    String filename = scanner.nextLine().isEmpty() ? "new_enc.txt" : scanner.nextLine();
    System.out.print("Enter file name containing enc text and key (default: encrypted_data.txt): ");
    String file = scanner.nextLine().isEmpty() ? "encrypted_data.txt" : scanner.nextLine();

    try(FileReader reader = new FileReader(filename)){
        Scanner fileScanner = new Scanner(reader);
        // String cipherText = fileScanner.nextLine();
        String encryptionKey = fileScanner.nextLine();
        String cipherText = fileScanner.nextLine();

        String encString = readSecondFile(file);
        
        System.out.println(encString);
        fileScanner.close();

        String plainText = decrypt(cipherText, encryptionKey,encString);
        System.out.println("Decrypted plain text: " + plainText);
        } catch (IOException e) {
            System.err.println("Error reading file or decrypting: " + e.getMessage());
        }
    

	
    }
}

