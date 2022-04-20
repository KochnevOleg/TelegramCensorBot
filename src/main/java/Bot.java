import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;


public class Bot  extends TelegramLongPollingBot {
    private final String botName = "NeDushniBot";
    private final String botToken = "5318974698:AAHVLDThln8JHMCKlnBE3c7UPQHie-ICB6Q";

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        String incomeMsg = EmojiParser.removeAllEmojis(update.getMessage().getText().toLowerCase());
        String chatID = update.getMessage().getChatId().toString();

        if (UpdateHandler.isContainTargetTag(incomeMsg)){
            execute(UpdateHandler.sendPoll(UpdateHandler.getListOfTargetTags(incomeMsg), chatID));
        }

        if(UpdateHandler.isContainStopWord(incomeMsg)) {
            String user = update.getMessage().getFrom().getUserName();
            int msgID = update.getMessage().getMessageId();
            execute(UpdateHandler.deleteMsg(chatID, msgID));
            execute(UpdateHandler.sendMessage(chatID, "Здесь было душнильское сообщение! \nДушнить пытался @" + user + "."));
        }
    }
}
