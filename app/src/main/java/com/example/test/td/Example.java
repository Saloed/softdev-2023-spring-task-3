package com.example.test.td;

//
// Copyright Aliaksei Levin (levlam@telegram.org), Arseny Smirnov (arseny30@gmail.com) 2014-2023
//
// Distributed under the Boost Software License, Version 1.0. (See accompanying
// file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)
//

import com.example.test.ChatViewModel;
import com.example.test.data.Author;
import com.example.test.data.ChatElement;
import com.example.test.data.ChatList;
import com.example.test.data.ChatType;
import com.example.test.data.Message;
import com.example.test.data.RecipientsData;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Example class for TDLib usage from Java.
 */
public final class Example {
    private static Client client = null;

    private static TdApi.AuthorizationState authorizationState = null;
    private static volatile boolean haveAuthorization = false;
    private static volatile boolean needQuit = false;
    private static volatile boolean canQuit = false;

    private static final Client.ResultHandler defaultHandler = new DefaultHandler();

    private static final Lock authorizationLock = new ReentrantLock();
    private static final Condition gotAuthorization = authorizationLock.newCondition();

    private static final ConcurrentMap<Long, TdApi.User> users = new ConcurrentHashMap<Long, TdApi.User>();
    private static final ConcurrentMap<Long, TdApi.BasicGroup> basicGroups = new ConcurrentHashMap<Long, TdApi.BasicGroup>();
    private static final ConcurrentMap<Long, TdApi.Supergroup> supergroups = new ConcurrentHashMap<Long, TdApi.Supergroup>();
    private static final ConcurrentMap<Integer, TdApi.SecretChat> secretChats = new ConcurrentHashMap<Integer, TdApi.SecretChat>();

    private static final ConcurrentMap<Long, TdApi.Chat> chats = new ConcurrentHashMap<Long, TdApi.Chat>();
    private static final NavigableSet<OrderedChat> mainChatList = new TreeSet<OrderedChat>();
    private static boolean haveFullMainChatList = false;

    private static final ConcurrentMap<Long, TdApi.UserFullInfo> usersFullInfo = new ConcurrentHashMap<Long, TdApi.UserFullInfo>();
    private static final ConcurrentMap<Long, TdApi.BasicGroupFullInfo> basicGroupsFullInfo = new ConcurrentHashMap<Long, TdApi.BasicGroupFullInfo>();
    private static final ConcurrentMap<Long, TdApi.SupergroupFullInfo> supergroupsFullInfo = new ConcurrentHashMap<Long, TdApi.SupergroupFullInfo>();

    private static final String newLine = System.getProperty("line.separator");
    private static final String commandsLine = "Enter command (gcs - GetChats, gc <chatId> - GetChat, me - GetMe, sm <chatId> <message> - SendMessage, lo - LogOut, q - Quit): ";
    private static volatile String currentPrompt = null;

    private static void print(String str) {
        if (currentPrompt != null) {
            System.out.println("");
        }
        System.out.println(str);
        if (currentPrompt != null) {
            System.out.print(currentPrompt);
        }
    }

    private static void setChatPositions(TdApi.Chat chat, TdApi.ChatPosition[] positions) {
        synchronized (mainChatList) {
            synchronized (chat) {
                for (TdApi.ChatPosition position : chat.positions) {
                    if (position.list.getConstructor() == TdApi.ChatListMain.CONSTRUCTOR) {
                        boolean isRemoved = mainChatList.remove(new OrderedChat(chat.id, position));
                        assert isRemoved;
                    }
                }

                chat.positions = positions;

                for (TdApi.ChatPosition position : chat.positions) {
                    if (position.list.getConstructor() == TdApi.ChatListMain.CONSTRUCTOR) {
                        boolean isAdded = mainChatList.add(new OrderedChat(chat.id, position));
                        assert isAdded;
                    }
                }
            }
        }
    }

    private static void onAuthorizationStateUpdated(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null) {
            Example.authorizationState = authorizationState;
        }
        switch (Example.authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                TdApi.TdlibParameters param = new TdApi.TdlibParameters();
                param.databaseDirectory = filePath;
                param.useMessageDatabase = true;
                param.useSecretChats = true;
                param.apiId = 94575;
                param.apiHash = "a3406de8d171bb422bb6ddf3bbd800e2";
                param.systemLanguageCode = "en";
                param.deviceModel = "Desktop";
                param.applicationVersion = "1.0";
                param.enableStorageOptimizer = true;
                TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters(param);

                client.send(request, new AuthorizationRequestHandler());
                break;
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR: {
                String phoneNumber = promptString("Please enter phone number: ");
                client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR: {
                client.send(new TdApi.CheckDatabaseEncryptionKey(), new AuthorizationRequestHandler());
            }
            case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR: {
                String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) Example.authorizationState).link;
                System.out.println("Please confirm this login link on another device: " + link);
                break;
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR: {
                String code = promptString("Please enter authentication code: ");
                client.send(new TdApi.CheckAuthenticationCode(code), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR: {
                String firstName = promptString("Please enter your first name: ");
                String lastName = promptString("Please enter your last name: ");
                client.send(new TdApi.RegisterUser(firstName, lastName), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
                String password = promptString("Please enter password: ");
                client.send(new TdApi.CheckAuthenticationPassword(password), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                haveAuthorization = true;
                authorizationLock.lock();
                try {
                    gotAuthorization.signal();
                } finally {
                    authorizationLock.unlock();
                }
                break;
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR:
                haveAuthorization = false;
                print("Logging out");
                break;
            case TdApi.AuthorizationStateClosing.CONSTRUCTOR:
                haveAuthorization = false;
                print("Closing");
                break;
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR:
                print("Closed");
                if (!needQuit) {
                    client = Client.create(new UpdateHandler(), null, null); // recreate client after previous has closed
                } else {
                    canQuit = true;
                }
                break;
            default:
                System.err.println("Unsupported authorization state:" + newLine + Example.authorizationState);
        }
    }

    private static int toInt(String arg) {
        int result = 0;
        try {
            result = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
        }
        return result;
    }

    private static long getChatId(String arg) {
        long chatId = 0;
        try {
            chatId = Long.parseLong(arg);
        } catch (NumberFormatException ignored) {
        }
        return chatId;
    }

    //    private static String promptString(String prompt) {
//        System.out.print(prompt);
//        currentPrompt = prompt;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        String str = "";
//        try {
//            str = reader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        currentPrompt = null;
//        return str;
//    }
    private static Function<String, String> promptCallback;

    private static String promptString(String prompt) {
        System.out.print(prompt);
        currentPrompt = prompt;
        String str = promptCallback.apply(currentPrompt);
        currentPrompt = null;
        return str;
    }

    private static void getCommand() {
        String command = promptString(commandsLine);
        String[] commands = command.split(" ", 2);
        try {
            switch (commands[0]) {
                case "gcs": {
                    int limit = 20;
                    if (commands.length > 1) {
                        limit = toInt(commands[1]);
                    }
                    getMainChatList(limit);
                    break;
                }
                case "gc":
                    client.send(new TdApi.GetChat(getChatId(commands[1])), defaultHandler);
                    break;
                case "me":
                    client.send(new TdApi.GetMe(), defaultHandler);
                    break;
                case "sm": {
                    String[] args = commands[1].split(" ", 2);
                    sendMessage(getChatId(args[0]), args[1]);
                    break;
                }
                case "lo":
                    haveAuthorization = false;
                    client.send(new TdApi.LogOut(), defaultHandler);
                    break;
                case "q":
                    needQuit = true;
                    haveAuthorization = false;
                    client.send(new TdApi.Close(), defaultHandler);
                    break;
                default:
                    System.err.println("Unsupported command: " + command);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            print("Not enough arguments");
        }
    }

    private static void getMainChatList(final int limit) {
        synchronized (mainChatList) {
            if (!haveFullMainChatList && limit > mainChatList.size()) {
                // send LoadChats request if there are some unknown chats and have not enough known chats
                client.send(new TdApi.LoadChats(new TdApi.ChatListMain(), limit - mainChatList.size()), new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        switch (object.getConstructor()) {
                            case TdApi.Error.CONSTRUCTOR:
                                if (((TdApi.Error) object).code == 404) {
                                    synchronized (mainChatList) {
                                        haveFullMainChatList = true;
                                    }
                                } else {
                                    System.err.println("Receive an error for LoadChats:" + newLine + object);
                                }
                                break;
                            case TdApi.Ok.CONSTRUCTOR:
                                // chats had already been received through updates, let's retry request
                                getMainChatList(limit);
                                break;
                            default:
                                System.err.println("Receive wrong response from TDLib:" + newLine + object);
                        }
                    }
                });
            }

//            java.util.Iterator<OrderedChat> iter = mainChatList.iterator();
//            System.out.println();
//            System.out.println("First " + limit + " chat(s) out of " + mainChatList.size() + " known chat(s):");
//            for (int i = 0; i < limit && i < mainChatList.size(); i++) {
//                long chatId = iter.next().chatId;
//                TdApi.Chat chat = chats.get(chatId);
//                synchronized (chat) {
//                    System.out.println(chatId + ": " + chat.title);
//                }
//            }
//            print("");
        }
    }

    private static void sendMessage(long chatId, String message) {
        // initialize reply markup just for testing
        TdApi.InlineKeyboardButton[] row = {new TdApi.InlineKeyboardButton("https://telegram.org?1", new TdApi.InlineKeyboardButtonTypeUrl()), new TdApi.InlineKeyboardButton("https://telegram.org?2", new TdApi.InlineKeyboardButtonTypeUrl()), new TdApi.InlineKeyboardButton("https://telegram.org?3", new TdApi.InlineKeyboardButtonTypeUrl())};
        TdApi.ReplyMarkup replyMarkup = new TdApi.ReplyMarkupInlineKeyboard(new TdApi.InlineKeyboardButton[][]{row, row, row});

        TdApi.InputMessageContent content = new TdApi.InputMessageText(new TdApi.FormattedText(message, null), false, true);
        client.send(new TdApi.SendMessage(chatId, 0, 0, null, replyMarkup, content), defaultHandler);
    }

    private static String filePath;

    public static void main(String path, Function<String, String> promptCallbackFunction) throws InterruptedException {
        filePath = path;
        promptCallback = promptCallbackFunction;
        // set log message handler to handle only fatal errors (0) and plain log messages (-1)
//        Client.setLogMessageHandler(0, new LogMessageHandler());
        Client.setLogVerbosityLevel(0);
//
//        // disable TDLib log and redirect fatal errors and plain log messages to a file
//        Client.execute(new TdApi.SetLogVerbosityLevel(0));
//        if (Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false))) instanceof TdApi.Error) {
//            throw new IOError(new IOException("Write access to the current directory is required"));
//        }

        // create client
        client = Client.create(new UpdateHandler(), null, null);

        // test Client.execute
        defaultHandler.onResult(Client.execute(new TdApi.GetTextEntities("@telegram /test_command https://telegram.org telegram.me @gif @test")));

        // main loop
//        while (!needQuit) {
//            // await authorization
//            authorizationLock.lock();
//            try {
//                while (!haveAuthorization) {
//                    gotAuthorization.await();
//                }
//            } finally {
//                authorizationLock.unlock();
//            }
//
//            while (haveAuthorization) {
//                getCommand();
//            }
//        }
//        while (!canQuit) {
//            Thread.sleep(1);
//        }
    }

    public static RecipientsData getUser(Long id) {
        TdApi.User user = Objects.requireNonNullElse(users.get(id), new TdApi.User());
        return new RecipientsData(user.firstName, String.valueOf(user.id));
    }

    public static ChatList getChats(Function<List<RecipientsData>, String> callbackFunc) throws InterruptedException, IllegalAccessError {

        authorizationLock.lock();
        try {
            if (!haveAuthorization) {
                throw new IllegalAccessError();
//                gotAuthorization.await();
            }
        } finally {
            authorizationLock.unlock();
        }


        getMainChatList(10);
        List<ChatElement> chatList = new ArrayList<>();
        synchronized (mainChatList) {
            java.util.Iterator<OrderedChat> iter = mainChatList.iterator();
            for (int i = 0; i < mainChatList.size(); i++) {
                long chatId = iter.next().chatId;
                TdApi.Chat chat = chats.get(chatId);
                synchronized (chat) {
                    // TODO: Проверить тип чатов
//                    client.send(new TdApi.GetBasicGroupFullInfo(chat.id), new FullChatHandler(callbackFunc)); // Можно случайно попросить не Basic chat, а личного или супергруппы

                    if (chat.lastMessage.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
                        String messageContent = ((TdApi.MessageText) chat.lastMessage.content).text.text;

                        chatList.add(new ChatElement(String.valueOf(chat.id), chat.title, "null", messageContent, new ArrayList<Message>(Arrays.asList(new Message(messageContent, senderToAuthor(chat.lastMessage.senderId)))), new ArrayList<RecipientsData>()));

                    } else if (chat.lastMessage.content.getConstructor() == TdApi.MessagePhoto.CONSTRUCTOR) {

                        String messageContent = "Фото";
                        chatList.add(new ChatElement(String.valueOf(chat.id), chat.title, "null", messageContent, new ArrayList<Message>(Arrays.asList(new Message(messageContent, senderToAuthor(chat.lastMessage.senderId)))), new ArrayList<RecipientsData>()));

                    } else if (chat.lastMessage.content.getConstructor() == TdApi.MessageVideo.CONSTRUCTOR) {

                        String messageContent = "Видео";
                        chatList.add(new ChatElement(String.valueOf(chat.id), chat.title, "null", messageContent, new ArrayList<Message>(Arrays.asList(new Message(messageContent, senderToAuthor(chat.lastMessage.senderId)))), new ArrayList<RecipientsData>()));

                    } else {
                        chatList.add(new ChatElement(String.valueOf(chat.id), chat.title, "null", "Контент не поддерживается", new ArrayList<Message>(), new ArrayList<RecipientsData>()));
                    }
                }
            }
        }
        for (ChatElement chat : chatList) {
            chat.setType(ChatType.TELEGRAM);
        }
        return new ChatList(chatList);
    }

    public static void getMe(Function<RecipientsData, String> callbackFunc) {
        client.send(new TdApi.GetMe(), new Client.ResultHandler(
        ) {
            @Override
            public void onResult(TdApi.Object object) {
                if (object.getConstructor() == TdApi.User.CONSTRUCTOR) {
                    TdApi.User user = (TdApi.User) object;
                    RecipientsData recipientsData = new RecipientsData(user.firstName, String.valueOf(user.id));
                    callbackFunc.apply(recipientsData);
                }
            }
        });
    }

    public static void getMessages(String chatId, Function<List<Message>, String> callbackFunc) throws InterruptedException {
        client.send(new TdApi.GetChatHistory(Long.parseLong(chatId), 0, 0, 500, false), new CallbackHandler(callbackFunc));
    }

    public static void sendMessageTd(long chatId, String message) {
        client.send(new TdApi.SendMessage(chatId, 0, 0, new TdApi.MessageSendOptions(), new TdApi.ReplyMarkupForceReply(), new TdApi.InputMessageText(new TdApi.FormattedText(message, new TdApi.TextEntity[0]), false, true)), defaultHandler);
    }

    private static class OrderedChat implements Comparable<OrderedChat> {
        final long chatId;
        final TdApi.ChatPosition position;

        OrderedChat(long chatId, TdApi.ChatPosition position) {
            this.chatId = chatId;
            this.position = position;
        }

        @Override
        public int compareTo(OrderedChat o) {
            if (this.position.order != o.position.order) {
                return o.position.order < this.position.order ? -1 : 1;
            }
            if (this.chatId != o.chatId) {
                return o.chatId < this.chatId ? -1 : 1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            OrderedChat o = (OrderedChat) obj;
            return this.chatId == o.chatId && this.position.order == o.position.order;
        }
    }

    private static Author senderToAuthor(TdApi.MessageSender sender) {


        switch (sender.getConstructor()) {
            case TdApi.MessageSenderUser.CONSTRUCTOR -> {
                TdApi.MessageSenderUser senderUser = (TdApi.MessageSenderUser) sender;//
//
                return new Author(Objects.requireNonNullElse(users.get(senderUser.userId), new TdApi.User()).firstName, String.valueOf(senderUser.userId));

            }
            case TdApi.MessageSenderChat.CONSTRUCTOR -> {
                TdApi.MessageSenderChat senderChat = (TdApi.MessageSenderChat) sender;

                return new Author("", String.valueOf(senderChat.chatId));
            }
        }
        return new Author("", "");
    }


    private static class FullChatHandler implements Client.ResultHandler {
        private static Function<List<RecipientsData>, String> func = null;

        FullChatHandler(Function<List<RecipientsData>, String> f) {
            func = f;
        }

        @Override
        public void onResult(TdApi.Object object) {
            if (object.getConstructor() == TdApi.BasicGroupFullInfo.CONSTRUCTOR) {
                TdApi.BasicGroupFullInfo groupFullInfo = (TdApi.BasicGroupFullInfo) object;
                List<RecipientsData> recipients = new ArrayList<>();
                for (TdApi.ChatMember member : groupFullInfo.members) {
                    recipients.add(new RecipientsData(member.memberId.toString(), member.status.toString()));
                }
                if (func != null) {
                    func.apply(recipients);
                }
            }
        }
    }

    private static class CallbackHandler implements Client.ResultHandler {
        private static Function<List<Message>, String> func;

        CallbackHandler(Function<List<Message>, String> f) {
            func = f;
        }


        @Override
        public void onResult(TdApi.Object object) {
            TdApi.Messages messages = (TdApi.Messages) object;
            List<Message> msgList = new ArrayList<Message>();
            for (TdApi.Message msg : messages.messages) {
                msgList.add(messageConverter(msg));

            }
            func.apply(msgList);
        }
    }

    public static Message messageConverter(TdApi.Message msg) {
        if (msg.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
            String content = ((TdApi.MessageText) msg.content).text.text;
            Message outputMessage = new Message(content, senderToAuthor(msg.senderId));
            outputMessage.setChatId(String.valueOf(msg.chatId));
            outputMessage.setTimestamp(msg.date);
            return outputMessage;

        } else if (msg.content.getConstructor() == TdApi.MessagePhoto.CONSTRUCTOR) {

            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) msg.content;
            String content = messagePhoto.caption.text;
            Message outputMessage = new Message(content, senderToAuthor(msg.senderId));

//            messagePhoto.photo.sizes[0]
            int preferredPhotoSize = 0;
            int maxPhotoSize = -1;// TODO: Можно добавить выбор качества картинок
            for (int i = 0; i < messagePhoto.photo.sizes.length; i++) {
                if (messagePhoto.photo.sizes[i].width > maxPhotoSize) {
                    preferredPhotoSize = i;
                    maxPhotoSize = messagePhoto.photo.sizes[i].width;
                }
            }
            client.send(new TdApi.DownloadFile(messagePhoto.photo.sizes[preferredPhotoSize].photo.id, 16, 0, 0, false), defaultHandler);
            if (messagePhoto.photo.minithumbnail != null) {
                outputMessage.setPreviewBitmap(messagePhoto.photo.minithumbnail.data);
            }
            String photoPath = messagePhoto.photo.sizes[preferredPhotoSize].photo.local.path;

            outputMessage.setImagePath(photoPath);
            outputMessage.setChatId(String.valueOf(msg.chatId));
            outputMessage.setTimestamp(msg.date);
            return outputMessage;
//
        } else if (msg.content.getConstructor() == TdApi.MessageVideo.CONSTRUCTOR) {
            String content = "Видео";

            Message outputMessage = new Message(content, senderToAuthor(msg.senderId));
            outputMessage.setChatId(String.valueOf(msg.chatId));
            outputMessage.setTimestamp(msg.date);
            return outputMessage;
        } else if (msg.content.getConstructor() == TdApi.MessageVoiceNote.CONSTRUCTOR) {
            String content = "Голосовые сообщения не поддерживаются";
            Message outputMessage = new Message(content, senderToAuthor(msg.senderId));
            outputMessage.setChatId(String.valueOf(msg.chatId));
            outputMessage.setTimestamp(msg.date);
            return outputMessage;
        } else {
            String content = "Сообщения этого типа не поддерживаются";
            Message outputMessage = new Message(content, senderToAuthor(msg.senderId));
            outputMessage.setChatId(String.valueOf(msg.chatId));
            outputMessage.setTimestamp(msg.date);
            return outputMessage;

        }
    }

    private static class DefaultHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            print(object.toString());
        }
    }

    private static BiFunction<String, Message, String> newMessageCallback = null;

    public static void subscribeOnNewMessages(BiFunction<String, Message, String> func) {
        newMessageCallback = func;
    }

    private static class UpdateHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                    onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState) object).authorizationState);
                    break;

                case TdApi.UpdateUser.CONSTRUCTOR:
                    TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
                    users.put(updateUser.user.id, updateUser.user);
                    break;
                case TdApi.UpdateUserStatus.CONSTRUCTOR: {
                    TdApi.UpdateUserStatus updateUserStatus = (TdApi.UpdateUserStatus) object;
                    TdApi.User user = users.get(updateUserStatus.userId);
                    synchronized (user) {
                        user.status = updateUserStatus.status;
                    }
                    break;
                }
                case TdApi.UpdateBasicGroup.CONSTRUCTOR:
                    TdApi.UpdateBasicGroup updateBasicGroup = (TdApi.UpdateBasicGroup) object;
                    basicGroups.put(updateBasicGroup.basicGroup.id, updateBasicGroup.basicGroup);
                    break;
                case TdApi.UpdateSupergroup.CONSTRUCTOR:
                    TdApi.UpdateSupergroup updateSupergroup = (TdApi.UpdateSupergroup) object;
                    supergroups.put(updateSupergroup.supergroup.id, updateSupergroup.supergroup);
                    break;
                case TdApi.UpdateSecretChat.CONSTRUCTOR:
                    TdApi.UpdateSecretChat updateSecretChat = (TdApi.UpdateSecretChat) object;
                    secretChats.put(updateSecretChat.secretChat.id, updateSecretChat.secretChat);
                    break;

                case TdApi.UpdateNewChat.CONSTRUCTOR: {
                    TdApi.UpdateNewChat updateNewChat = (TdApi.UpdateNewChat) object;
                    TdApi.Chat chat = updateNewChat.chat;
                    synchronized (chat) {
                        chats.put(chat.id, chat);

                        TdApi.ChatPosition[] positions = chat.positions;
                        chat.positions = new TdApi.ChatPosition[0];
                        setChatPositions(chat, positions);
                    }
                    break;
                }
                case TdApi.UpdateChatTitle.CONSTRUCTOR: {
                    TdApi.UpdateChatTitle updateChat = (TdApi.UpdateChatTitle) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.title = updateChat.title;
                    }
                    break;
                }
                case TdApi.UpdateChatPhoto.CONSTRUCTOR: {
                    TdApi.UpdateChatPhoto updateChat = (TdApi.UpdateChatPhoto) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.photo = updateChat.photo;
                    }
                    break;
                }
                case TdApi.UpdateChatLastMessage.CONSTRUCTOR: {
                    TdApi.UpdateChatLastMessage updateChat = (TdApi.UpdateChatLastMessage) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.lastMessage = updateChat.lastMessage;
                        setChatPositions(chat, updateChat.positions);
                    }
                    break;
                }
                case TdApi.UpdateChatPosition.CONSTRUCTOR: {
                    TdApi.UpdateChatPosition updateChat = (TdApi.UpdateChatPosition) object;
                    if (updateChat.position.list.getConstructor() != TdApi.ChatListMain.CONSTRUCTOR) {
                        break;
                    }

                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        int i;
                        for (i = 0; i < chat.positions.length; i++) {
                            if (chat.positions[i].list.getConstructor() == TdApi.ChatListMain.CONSTRUCTOR) {
                                break;
                            }
                        }
                        TdApi.ChatPosition[] new_positions = new TdApi.ChatPosition[chat.positions.length + (updateChat.position.order == 0 ? 0 : 1) - (i < chat.positions.length ? 1 : 0)];
                        int pos = 0;
                        if (updateChat.position.order != 0) {
                            new_positions[pos++] = updateChat.position;
                        }
                        for (int j = 0; j < chat.positions.length; j++) {
                            if (j != i) {
                                new_positions[pos++] = chat.positions[j];
                            }
                        }
                        assert pos == new_positions.length;

                        setChatPositions(chat, new_positions);
                    }
                    break;
                }
                case TdApi.UpdateChatReadInbox.CONSTRUCTOR: {
                    TdApi.UpdateChatReadInbox updateChat = (TdApi.UpdateChatReadInbox) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.lastReadInboxMessageId = updateChat.lastReadInboxMessageId;
                        chat.unreadCount = updateChat.unreadCount;
                    }
                    break;
                }
                case TdApi.UpdateChatReadOutbox.CONSTRUCTOR: {
                    TdApi.UpdateChatReadOutbox updateChat = (TdApi.UpdateChatReadOutbox) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.lastReadOutboxMessageId = updateChat.lastReadOutboxMessageId;
                    }
                    break;
                }
                case TdApi.UpdateChatUnreadMentionCount.CONSTRUCTOR: {
                    TdApi.UpdateChatUnreadMentionCount updateChat = (TdApi.UpdateChatUnreadMentionCount) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.unreadMentionCount = updateChat.unreadMentionCount;
                    }
                    break;
                }
                case TdApi.UpdateMessageMentionRead.CONSTRUCTOR: {
                    TdApi.UpdateMessageMentionRead updateChat = (TdApi.UpdateMessageMentionRead) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.unreadMentionCount = updateChat.unreadMentionCount;
                    }
                    break;
                }
                case TdApi.UpdateChatReplyMarkup.CONSTRUCTOR: {
                    TdApi.UpdateChatReplyMarkup updateChat = (TdApi.UpdateChatReplyMarkup) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.replyMarkupMessageId = updateChat.replyMarkupMessageId;
                    }
                    break;
                }
                case TdApi.UpdateChatDraftMessage.CONSTRUCTOR: {
                    TdApi.UpdateChatDraftMessage updateChat = (TdApi.UpdateChatDraftMessage) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.draftMessage = updateChat.draftMessage;
                        setChatPositions(chat, updateChat.positions);
                    }
                    break;
                }
                case TdApi.UpdateChatPermissions.CONSTRUCTOR: {
                    TdApi.UpdateChatPermissions update = (TdApi.UpdateChatPermissions) object;
                    TdApi.Chat chat = chats.get(update.chatId);
                    synchronized (chat) {
                        chat.permissions = update.permissions;
                    }
                    break;
                }
                case TdApi.UpdateChatNotificationSettings.CONSTRUCTOR: {
                    TdApi.UpdateChatNotificationSettings update = (TdApi.UpdateChatNotificationSettings) object;
                    TdApi.Chat chat = chats.get(update.chatId);
                    synchronized (chat) {
                        chat.notificationSettings = update.notificationSettings;
                    }
                    break;
                }
                case TdApi.UpdateChatDefaultDisableNotification.CONSTRUCTOR: {
                    TdApi.UpdateChatDefaultDisableNotification update = (TdApi.UpdateChatDefaultDisableNotification) object;
                    TdApi.Chat chat = chats.get(update.chatId);
                    synchronized (chat) {
                        chat.defaultDisableNotification = update.defaultDisableNotification;
                    }
                    break;
                }
                case TdApi.UpdateChatIsMarkedAsUnread.CONSTRUCTOR: {
                    TdApi.UpdateChatIsMarkedAsUnread update = (TdApi.UpdateChatIsMarkedAsUnread) object;
                    TdApi.Chat chat = chats.get(update.chatId);
                    synchronized (chat) {
                        chat.isMarkedAsUnread = update.isMarkedAsUnread;
                    }
                    break;
                }
                case TdApi.UpdateChatIsBlocked.CONSTRUCTOR: {
                    TdApi.UpdateChatIsBlocked update = (TdApi.UpdateChatIsBlocked) object;
                    TdApi.Chat chat = chats.get(update.chatId);
                    synchronized (chat) {
                        chat.isBlocked = update.isBlocked;
                    }
                    break;
                }
                case TdApi.UpdateChatHasScheduledMessages.CONSTRUCTOR: {
                    TdApi.UpdateChatHasScheduledMessages update = (TdApi.UpdateChatHasScheduledMessages) object;
                    TdApi.Chat chat = chats.get(update.chatId);
                    synchronized (chat) {
                        chat.hasScheduledMessages = update.hasScheduledMessages;
                    }
                    break;
                }

                case TdApi.UpdateUserFullInfo.CONSTRUCTOR:
                    TdApi.UpdateUserFullInfo updateUserFullInfo = (TdApi.UpdateUserFullInfo) object;
                    usersFullInfo.put(updateUserFullInfo.userId, updateUserFullInfo.userFullInfo);
                    break;
                case TdApi.UpdateBasicGroupFullInfo.CONSTRUCTOR:
                    TdApi.UpdateBasicGroupFullInfo updateBasicGroupFullInfo = (TdApi.UpdateBasicGroupFullInfo) object;
                    basicGroupsFullInfo.put(updateBasicGroupFullInfo.basicGroupId, updateBasicGroupFullInfo.basicGroupFullInfo);
                    break;
                case TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR:
                    TdApi.UpdateSupergroupFullInfo updateSupergroupFullInfo = (TdApi.UpdateSupergroupFullInfo) object;
                    supergroupsFullInfo.put(updateSupergroupFullInfo.supergroupId, updateSupergroupFullInfo.supergroupFullInfo);
                    break;
                case TdApi.UpdateNewMessage.CONSTRUCTOR: // Мой код
                    TdApi.UpdateNewMessage update = (TdApi.UpdateNewMessage) object;
                    newMessageCallback.apply(String.valueOf(update.message.chatId), messageConverter(update.message));

                default:
                    // print("Unsupported update:" + newLine + object);
            }

        }
    }

    private static class AuthorizationRequestHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR:
                    System.err.println("Receive an error:" + newLine + object);
                    onAuthorizationStateUpdated(null); // repeat last action
                    break;
                case TdApi.Ok.CONSTRUCTOR:
                    // result is already received through UpdateAuthorizationState, nothing to do
                    break;
                default:
                    System.err.println("Receive wrong response from TDLib:" + newLine + object);
            }
        }

    }


    private static void onFatalError(String errorMessage) {
        final class ThrowError implements Runnable {
            private final String errorMessage;
            private final AtomicLong errorThrowTime;

            private ThrowError(String errorMessage, AtomicLong errorThrowTime) {
                this.errorMessage = errorMessage;
                this.errorThrowTime = errorThrowTime;
            }

            @Override
            public void run() {
                if (isDatabaseBrokenError(errorMessage) || isDiskFullError(errorMessage) || isDiskError(errorMessage)) {
                    processExternalError();
                    return;
                }

                errorThrowTime.set(System.currentTimeMillis());
                throw new ClientError("TDLib fatal error: " + errorMessage);
            }

            private void processExternalError() {
                errorThrowTime.set(System.currentTimeMillis());
                throw new ExternalClientError("Fatal error: " + errorMessage);
            }

            final class ClientError extends Error {
                private ClientError(String message) {
                    super(message);
                }
            }

            final class ExternalClientError extends Error {
                public ExternalClientError(String message) {
                    super(message);
                }
            }

            private boolean isDatabaseBrokenError(String message) {
                return message.contains("Wrong key or database is corrupted") ||
                        message.contains("SQL logic error or missing database") ||
                        message.contains("database disk image is malformed") ||
                        message.contains("file is encrypted or is not a database") ||
                        message.contains("unsupported file format") ||
                        message.contains("Database was corrupted and deleted during execution and can't be recreated");
            }

            private boolean isDiskFullError(String message) {
                return message.contains("PosixError : No space left on device") ||
                        message.contains("database or disk is full");
            }

            private boolean isDiskError(String message) {
                return message.contains("I/O error") || message.contains("Structure needs cleaning");
            }
        }

        final AtomicLong errorThrowTime = new AtomicLong(Long.MAX_VALUE);
        new Thread(new ThrowError(errorMessage, errorThrowTime), "TDLib fatal error thread").start();

        // wait at least 10 seconds after the error is thrown
        while (errorThrowTime.get() >= System.currentTimeMillis() - 10000) {
            try {
                Thread.sleep(1000 /* milliseconds */);
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
