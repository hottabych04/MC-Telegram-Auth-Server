package auth.plugin.mc.service;

import auth.plugin.mc.controller.AuthBot;
import auth.plugin.mc.model.Account;
import auth.plugin.mc.model.LoginResp;
import auth.plugin.mc.model.RegisterResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    private final RabbitTemplate rabbitTemplate;

    private AuthBot authBot;

    public void registerBot(AuthBot bot){
        this.authBot = bot;
    }

    public void processUpdate(Update update){
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasCallbackQuery()){
            processCallbackUpdate(update);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            processTextMessage(update);
        }
    }

    private void processCallbackUpdate(Update update) {

        CallbackQuery query = update.getCallbackQuery();

        if (query.getData().startsWith("login")){
            sendLoginRequest(query);
        }

        if (query.getData().startsWith("hash")){
            sendRegisterRequest(query);
        }

    }

    private void sendLoginRequest(CallbackQuery query) {
        String[] split = query.getData().split(" ");
        int message_id = query.getMessage().getMessageId();
        long chat_id = query.getMessage().getChatId();

        String uuid = split[1];

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chat_id)
                .messageId(message_id)
                .text("Waiting for login")
                .build();

        editMessage.setReplyMarkup(null);

        authBot.execute(editMessage);

        rabbitTemplate.convertAndSend("auth_resp_exchange", "login_routing_key", new LoginResp(uuid));

    }

    private void sendRegisterRequest(CallbackQuery query) {

        String[] split = query.getData().split(" ");
        int message_id = query.getMessage().getMessageId();
        long chat_id = query.getMessage().getChatId();

        String hash = split[1];

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chat_id)
                .messageId(message_id)
                .text("Waiting for registration")
                .build();

        editMessage.setReplyMarkup(null);

        authBot.execute(editMessage);

        RegisterResp registerResp = RegisterResp.builder()
                .telegramId(String.valueOf(chat_id))
                .hash(hash)
                .build();

        rabbitTemplate.convertAndSend("auth_resp_exchange", "register_routing_key", registerResp);

    }

    private void processTextMessage(Update update) {

        Message message = update.getMessage();

        if (message.getText().equals("/start")){
            emptyStartCommand(message);
        }

        if (message.getText().startsWith("/start")){
            startCommandWithParam(message);
        }

    }

    private void startCommandWithParam(Message message) {

        String[] s = message.getText().split(" ");

        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("Для привязки этого акканта телеграм к профилю на сервере нажмите на кнопку ниже")
                .build();

        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("Регистрация")
                .callbackData("hash " + s[1])
                .build();

        InlineKeyboardRow rowInline = new InlineKeyboardRow(button);

        List<InlineKeyboardRow> rowsInline = new ArrayList<>();

        rowsInline.add(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(rowsInline);

        sendMessage.setReplyMarkup(markupInline);

        authBot.execute(sendMessage);

    }

    private void emptyStartCommand(Message message) {

        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("Что бы начать регистрацию зайдите на сервер :)")
                .build();

        authBot.execute(sendMessage);

    }

    @RabbitListener(queues = "login_req_queue")
    public void consumeLoginRequest(Account account){ sendLoginInvite(account);}

    private void sendLoginInvite(Account account) {

        String chat_id = account.getTelegramId();
        String username = account.getUsername();
        String uuid = account.getUuid();

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chat_id)
                .text("Привет " + username + "! Для подтврждения авторизации нажми на кнопку ниже!")
                .build();

        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("Авторизоваться!")
                .callbackData("login " + uuid)
                .build();

        InlineKeyboardRow rowInline = new InlineKeyboardRow(button);

        List<InlineKeyboardRow> rowsInline = new ArrayList<>();

        rowsInline.add(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(rowsInline);

        sendMessage.setReplyMarkup(markupInline);

        authBot.execute(sendMessage);

    }

}
