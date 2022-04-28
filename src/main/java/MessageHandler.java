import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class MessageHandler {

    private final String msg;
    private final String chatID;
    private final int msgID;
    private final String userName;
    private final String userFirstName;
    private final String userLastName;

    public MessageHandler(String msg, String chatID, int msgID, String userName, String userFirstName, String userLastName) {
        this.msg = msg;
        this.chatID = chatID;
        this.msgID = msgID;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    protected boolean isContainTargetTag() {
        String[] splitMsg = msg.split("[ ?,:;!]");
        for (String s : splitMsg) {
            if (FileWorker.getTargetTags().contains(s)) {
                break;
            }
            return false;
        }
        return true;
    }

    protected boolean isContainStopWord() {
        String[] splitMsg = msg.split("[ ?,:;!]");
        for (String s : splitMsg) {
            if (FileWorker.getStopList().contains(s)) {
                break;
            }
            return false;
        }
        return true;
    }

    protected SendMessage handleMessage() {
        SendMessage sm = new SendMessage();
        if (this.isContainTargetTag()) {
            String tag = FileWorker.getTargetTag(this.msg);
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(
                    Arrays.asList((InlineKeyboardButton.builder().text("Да").callbackData("yes").build()),
                            InlineKeyboardButton.builder().text("Нет").callbackData("no").build()));

            sm = SendMessage.builder()
                    .chatId(String.valueOf(chatID))
                    .text("Зафиксирована душнильская активность! " +
                            "\nСледует ли включить слово " + tag + " в список душнильских слов?")
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build();

            FileWorker.toPollContainerWriter(msgID + 1); // TODO костыль с +1
            FileWorker.targetTransfer(tag, "targetTags.txt", "inPollingProcess.txt");
        } else if (this.isContainStopWord()) {
            if (userName == null && userLastName != null) {
                sm = SendMessage.builder()
                        .chatId(chatID)
                        .text("Здесь было душнильское сообщение! \nДушнить пытался \uD83D\uDC13 " + userFirstName + " " + userLastName + ".")
                        .build();
            } else if (userName != null) {
                sm = SendMessage.builder()
                        .chatId(chatID)
                        .text("Здесь было душнильское сообщение! \nДушнить пытался \uD83D\uDC13 @" + userName + ".")
                        .build();
            } else {
                sm = SendMessage.builder()
                        .chatId(chatID)
                        .text("Здесь было душнильское сообщение! \nДушнить пытался \uD83D\uDC13 " + userFirstName + ".")
                        .build();
            }
        }
        return sm;
    }
}
