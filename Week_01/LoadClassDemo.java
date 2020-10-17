import java.io.*;

public class LoadClassDemo extends ClassLoader {
    public LoadClassDemo(ClassLoader parent) {
        super(parent);
    }

    public LoadClassDemo() {
        super();
    }

    public static void main(String[] args) throws Exception {
        LoadClassDemo demo = new LoadClassDemo(Hello.class.getClassLoader());
        byte[] bytes = demo.read("./Hello.xlass");
        for (int i = 0;i < bytes.length;i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
//        Hello hello = (Hello) demo.loadClass("Hello", bytes).newInstance();
//        hello.hello();
        Class<?> clazz = demo.loadClass("Hello", bytes);
        java.lang.reflect.Method method = clazz.getDeclaredMethod("hello");
        method.invoke(clazz.newInstance());
    }

    private static byte[] read(String file) throws IOException {
        File f = new File(file);
        if (!f.exists()) {
            throw new FileNotFoundException(file);
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
             BufferedInputStream in = new BufferedInputStream(new FileInputStream(f))) {
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Class<?> loadClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }
}