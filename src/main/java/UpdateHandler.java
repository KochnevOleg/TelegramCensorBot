import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.io.*;
import java.util.*;

public class UpdateHandler {

    @SneakyThrows
    protected static Set<String> getTargetTags() {
        Set<String> targetTags = new TreeSet<>();
        String path = "targetTags.txt";
        File file = new File(path);

        Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                targetTags.add(scanner.nextLine());
            }
            scanner.close();

        return targetTags;
    }

    @SneakyThrows
     protected static Set<String> getStopList() {
        Set<String> stopList = new TreeSet<>();
        String path = "stopList.txt";
        File file = new File(path);

        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            stopList.add(scanner.nextLine());
        }
        scanner.close();

        return stopList;
     }

    protected static SendPoll sendPoll(List<String> targetTags, String chatID) {
        List<String> answers = new ArrayList<>();
        answers.add("Да");
        answers.add("Нет");

        String[] tags = new String[targetTags.size()];
        for(int i = 0; i < targetTags.size(); i++) {
            tags[i] = targetTags.get(i);
        }

        SendPoll poll = new SendPoll();
        poll.setChatId(chatID);
        poll.setQuestion("Зафиксирована душнильская активность! " +
                "\nСледует ли включить " + Arrays.toString(tags) + " в список душнильских слов?");
        poll.setOptions(answers);
        poll.disableNotification();
        poll.setIsAnonymous(true);
        poll.setOpenPeriod(600);

        return poll;
    }

    protected static SendMessage sendMessage(String chatID, String msg) {
        SendMessage response = new SendMessage();
        response.setChatId(chatID);
        response.setText(msg);

        return response;
    }

    protected static DeleteMessage deleteMsg(String chatID, int msgID) {
        DeleteMessage dm = new DeleteMessage();
        dm.setChatId(chatID);
        dm.setMessageId(msgID);
        return dm;
    }

    protected static boolean isContainTargetTag(String msg) {
        String[] splitMsg = msg.split("[ ?,:;!]");
        for (String s : splitMsg) {
            if (getTargetTags().contains(s)) {
                break;
            }
            return false;
        }
        return true;
    }

    protected static List<String> getListOfTargetTags(String msg) {
        String[] splitMsg = msg.split("[ ?,:;!]");
        List<String> listOfTags = new ArrayList<>();
        for (String s : splitMsg) {
            if (getTargetTags().contains(s)) {
                listOfTags.add(s);
            }
        }
        return listOfTags;
    }

    protected static boolean isContainStopWord(String msg) {
        String[] splitMsg = msg.split("[ ?,:;!]");
        for (String s : splitMsg) {
            if (getStopList().contains(s)) {
                break;
            }
            return false;
        }
        return true;
    }

    @SneakyThrows
    protected static void toStopListTransfer(String stopWord) {
        Set<String> stopList = new TreeSet<>();
        String path = "stopList.txt";
        File file = new File(path);

        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            stopList.add(scanner.nextLine());
        }
        stopList.add(stopWord);
        scanner.close();

        FileWriter fw = new FileWriter(path);
        for(String word : stopList) {
            fw.write(word + "\n");
        }
        fw.close();
    }
}
