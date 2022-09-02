import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWindow {
    // адрес сервера
    private static final String SERVER_HOST = "localhost";
    // порт
    private static final int SERVER_PORT = 7777;
    // клиентский сокет
    private Socket clientSocket;
    private final Scanner scanner = new Scanner(System.in);
    // входящее сообщение
    private Scanner inMessage;
    // исходящее сообщение
    private PrintWriter outMessage;
    // следующие поля отвечают за элементы формы
    // имя клиента
    private String clientName = "";

    // конструктор
    public ClientWindow() {
        try {
            // получаем имя клиента
            System.out.println("Введите ваше имя");
            clientName = scanner.nextLine();
            // подключаемся к серверу
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // в отдельном потоке начинаем работу с сервером
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // бесконечный цикл
                    while (true) {
                        // если есть входящее сообщение
                        if (inMessage.hasNext()) {
                            // считываем его
                            String inMes = inMessage.nextLine();
                            System.out.println(inMes);
                        }
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // бесконечный цикл
                    while (true) {
                        String message = scanner.nextLine();
                        sendMsg(clientName + ": " + message);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }).start();
    }

    // отправка сообщения
    public void sendMsg(String messageStr) {
        outMessage.println(messageStr);
        outMessage.flush();

    }
}
