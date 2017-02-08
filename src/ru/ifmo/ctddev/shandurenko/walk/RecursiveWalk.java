package ru.ifmo.ctddev.shandurenko.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Created by RINES on 08.02.17.
 */
public class RecursiveWalk {

    private final static String NULL_HASH = "00000000";
    private final static boolean DEBUG = false;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            log("At least 2 arguments required!");
            return;
        }
        File input = new File(args[0]), output = new File(args[1]);
        if (!input.exists()) {
            log("Input file %s doesn't exist!", args[0]);
            return;
        }
        if (!output.exists()) {
            try {
                output.createNewFile();
            } catch (IOException ex) {
                warn("Output file %s can't be created!", args[1]);
                return;
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(input.toPath(), StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(output.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                process(new File(line), writer);
            }
        } catch (IOException ex) {
            warn("Can not read from the input file or write to the output file!");
            return;
        }
    }

    private static void process(File file, BufferedWriter writer) throws IOException {
        if (file.isDirectory()) {
            try {
                for (File child : file.listFiles()) {
                    process(child, writer);
                }
            } catch (NullPointerException ex) {
                warn("Can not read children of directory %s.", file.toString());
            }
        } else {
            writer.write(hash(file) + " " + file.toString());
            writer.newLine();
        }
    }

    private static String hash(File file) {
        long nano = System.currentTimeMillis();
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            byte[] slice = new byte[1 << 13];
            int size, hash = (int) 2166136261l;
            while ((size = inputStream.read(slice)) != -1) {
                hash = calculatePartialHash(hash, slice, size);
            }
            String result = String.format("%08x", hash);
            if(DEBUG) {
                log("[Debug] Hash of %s (size: %dMB) is %s (calculation took %dms)",
                        file.getName(), Files.size(file.toPath()) >> 20, result, System.currentTimeMillis() - nano);
            }
            return result;
        } catch (IOException e) {
            warn("Can not read file %s (does it exist?).", file.toString());
            return NULL_HASH;
        }
    }

    private static int calculatePartialHash(int hash, byte[] bytes, int sliceSize) {
        for (int i = 0; i < sliceSize; ++i) {
            hash *= 16777619;
            hash ^= bytes[i] & 0xFF;
        }
        return hash;
    }

    private static void log(String message, Object... args) {
        System.out.println(String.format(message, args));
    }

    private static void warn(String message, Object... args) {
        System.err.println(String.format(message, args));
    }

}
