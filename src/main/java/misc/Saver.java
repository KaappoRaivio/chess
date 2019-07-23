package misc;

import java.io.*;

public class Saver<T extends Serializable> implements Serializable {
    public String save (T object, String path, boolean relative) {
        if (relative) {
//            path = System.getProperty("user.dir") + "/src/main/resources/boards/" + name + ".ser";
            path = System.getProperty("user.dir") + path;
        }

        File file = new File(path);

        try {
            if (file.createNewFile()) {
                System.out.println("Creating new file " + path);

            } else {
                System.out.println("File " + path + " already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            FileOutputStream fileOut = new FileOutputStream(path);

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();

            fileOut.close();

            System.out.println("Saved " + object.getClass() + " to: " + path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }

    public T fromFile (String path, boolean relative) {
        String absolutePath;

        if (relative) {
            absolutePath = System.getProperty("user.dir") + path;
        } else {
            absolutePath = path;
        }


        T recovered;
        try {
            FileInputStream fileIn = new FileInputStream(absolutePath);

            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                Object obj = in.readObject();

                //noinspection unchecked
                recovered = (T) obj;

            } catch (ClassCastException | ClassNotFoundException e) {
                throw new RuntimeException("No valid object found!");
            }
            in.close();

            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return recovered;
    }

    public T deepCopy (T object) {
        T newObject;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();

            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                newObject = (T) objectInputStream.readObject();
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new RuntimeException("Exception in deepcopying " + object);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newObject;
    }
}
