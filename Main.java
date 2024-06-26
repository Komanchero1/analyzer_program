import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    //создаются 3 потокобезопасные очереди
    static BlockingQueue<String> names1 = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> names2 = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> names3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        //создается список для хранения потоков
        List<Thread> threads = new ArrayList<>();

        //создается поток для заполнения очередей
        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    //добавляем сгенерированные строки в очередь с помощью
                    // блокирующего метода  offer, если очередь заполнена метод не даст
                    // потоку простаивать в ожидании освобождения очереди
                    boolean added1 = names1.offer(generateText("abc", 10_000), 50, TimeUnit.MILLISECONDS);
                    boolean added2 = names2.offer(generateText("abc", 10_000), 50, TimeUnit.MILLISECONDS);
                    boolean added3 = names3.offer(generateText("abc", 10_000), 50, TimeUnit.MILLISECONDS);
                    if (!added1 && !added2 && !added3) {
                        // Если ни один элемент не был добавлен, значит все очереди полны
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread1.start();//стартует поток
        threads.add(thread1);//добавляется поток в список


        //создается поток в котором реализуется логика поиска в строке самой
        // длиной  подстроки состоящей из букв "а"
        Thread thread2 = new Thread(() -> {
            String maxSize = "";//переменная для хранения самой длинной подстроки
            String currentMax = "";//для хранения текущей  максимальной строки
            for (int i = 0; i < 100; i++) {
                try {
                    //находится в текущей строке максимально длинная подстрока
                    String str1 = largestStringOfLetters(names1.take(), 'a');
                    String str2 = largestStringOfLetters(names2.take(), 'a');
                    String str3 = largestStringOfLetters(names3.take(), 'a');
                    if (str1.length() > currentMax.length()) { //если найденная подстрока больше текущей
                        currentMax = str1; //добовляется в currentMax
                    }
                    if (str2.length() > currentMax.length()) {//то же из второй очереди
                        currentMax = str2;
                    }
                    if (str3.length() > currentMax.length()) {//то же из 3 очереди
                        currentMax = str3;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            maxSize = currentMax;//сохраняется подстрока как максимально длинная из трех очередей
            System.out.println("Самая длинная часть строки с подряд идущими 'а' это: "
                    + maxSize + " -  " + maxSize.length());
        });
        thread2.start();
        threads.add(thread2);


        //создается поток в котором реализуется логика поиска в строке самой
        // длиной  подстроки состоящей из букв "b"
        Thread thread3 = new Thread(() -> {
            String maxSize = "";
            String currentMax = "";
            for (int i = 0; i < 100; i++) {
                try {
                    String str1 = largestStringOfLetters(names1.take(), 'b');
                    String str2 = largestStringOfLetters(names2.take(), 'b');
                    String str3 = largestStringOfLetters(names3.take(), 'b');
                    if (str1.length() > currentMax.length()) {
                        currentMax = str1;
                    }
                    if (str2.length() > currentMax.length()) {
                        currentMax = str2;
                    }
                    if (str3.length() > currentMax.length()) {
                        currentMax = str3;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            maxSize = currentMax;
            System.out.println("Самая длинная часть строки с подряд идущими 'b' это: "
                    + maxSize + " -  " + maxSize.length());
        });
        thread3.start();
        threads.add(thread3);


        //создается поток в котором реализуется логика поиска в строке самой
        // длиной  подстроки состоящей из букв "c"
        Thread thread4 = new Thread(() -> {
            String maxSize = "";
            String currentMax = "";
            for (int i = 0; i < 100; i++) {
                try {
                    String str1 = largestStringOfLetters(names1.take(), 'c');
                    String str2 = largestStringOfLetters(names2.take(), 'c');
                    String str3 = largestStringOfLetters(names3.take(), 'c');
                    if (str1.length() > currentMax.length()) {
                        currentMax = str1;
                    }
                    if (str2.length() > currentMax.length()) {
                        currentMax = str2;
                    }
                    if (str3.length() > currentMax.length()) {
                        currentMax = str3;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            maxSize = currentMax;
            System.out.println("Самая длинная часть строки с подряд идущими 'c' это: "
                    + maxSize + " -  " + maxSize.length());
        });
        thread4.start();
        threads.add(thread4);

        //ожидаем окончания работы всех потоков
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    //метод реализующий логику нахождения в строке максимально
    // длиной подстроки из повторяющихся заданных букв
    public static String largestStringOfLetters(String text, char liter) {
        // переменная для хранения максимальной длины подстроки
        int maxLength = 0;
        //переменная для хранения начального индекса максимальной подстроки
        int startIndex = 0;
        //переменная для хранения текущей длины подстроки
        int currentLength = 0;
        //переменная для хранения начального индекса текущей подстроки
        int currentStart = 0;
        //в цикле проходим по строке
        for (int i = 0; i < text.length(); i++) {
            //если текущий символ совпадает с искомым увеличиваем текущую длину подстроки
            if (text.charAt(i) == liter) {
                currentLength++;
            } else {
                // Если текущая подстрока закончилась и её длина больше максимальной,
                // обновляем `maxLength` и `startIndex`
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    startIndex = currentStart;
                }
                // Сброс `currentLength` и установка `currentStart` на следующий
                // символ после текущего
                currentLength = 0;
                currentStart = i + 1;
            }
        }

        // Проверка и обработка случая, когда самая длинная подстрока
        // находится в конце основной строки`.
        if (currentLength > maxLength) {
            maxLength = currentLength;
            startIndex = currentStart;
        }
        //возвращение самой длинной подстроки
        return text.substring(startIndex, startIndex + maxLength);
    }

    //метод для рандомной генерации строк из заданных символов и заданной строки
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}