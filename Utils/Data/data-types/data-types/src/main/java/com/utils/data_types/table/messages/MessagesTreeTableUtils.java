package com.utils.data_types.table.messages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.utils.log.messages.Message;
import com.utils.log.messages.MessageType;
import com.utils.log.messages.Messages;

public final class MessagesTreeTableUtils {

	private MessagesTreeTableUtils() {
	}

	public static void fillTableRowDataMessageList(
			final Messages messages,
			final List<TableRowDataMessage> tableRowDataMessageList) {

		final List<Message> messageList = messages.createDisplayMessageList();
		messageList.sort(Comparator.naturalOrder());

		String lastMessageCategory = null;
		TableRowDataMessage tableRowDataMessageCategory = null;
		for (final Message message : messageList) {

			final MessageType messageType = message.getMessageType();
			final String messageCategory = message.getMessageCategory();
			if (!messageCategory.equals(lastMessageCategory)) {

				tableRowDataMessageCategory = new TableRowDataMessage(true, messageType, messageCategory);
				tableRowDataMessageList.add(tableRowDataMessageCategory);
				lastMessageCategory = messageCategory;
			}

			final String messageString = message.getMessageString();
			final TableRowDataMessage tableRowDataMessage =
					new TableRowDataMessage(false, messageType, messageString);
			tableRowDataMessageCategory.getChildTableRowDataMessageList().add(tableRowDataMessage);
		}
	}

	public static void fillAllTableRowDataMessageList(
			final Messages messages,
			final List<TableRowDataMessage> allTableRowDataMessageList) {

		final List<TableRowDataMessage> tableRowDataMessageList = new ArrayList<>();
		fillTableRowDataMessageList(messages, tableRowDataMessageList);

		fillAllTableRowDataMessageListRec(tableRowDataMessageList, allTableRowDataMessageList);
	}

	private static void fillAllTableRowDataMessageListRec(
			final List<TableRowDataMessage> tableRowDataMessageList,
			final List<TableRowDataMessage> allTableRowDataMessageList) {

		for (final TableRowDataMessage tableRowDataMessage : tableRowDataMessageList) {

			allTableRowDataMessageList.add(tableRowDataMessage);

			final List<TableRowDataMessage> childTableRowDataMessageList =
					tableRowDataMessage.getChildTableRowDataMessageList();
			fillAllTableRowDataMessageListRec(childTableRowDataMessageList, allTableRowDataMessageList);
		}
	}
}
