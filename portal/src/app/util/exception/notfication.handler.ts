import { NotificationProperties, NotificationService } from "tgocp-ng/dist";
import { Injectable } from "@angular/core";

@Injectable()
export class NotificationHandler {
  constructor(private notificationService: NotificationService) {}

  notify(message: any) {
    let notification = new NotificationProperties();
    let content: String[] = [];
    notification.userMessage = message.userMessage ? message.userMessage : "";
    notification.type = message.severity ? message.severity : "";
    notification.title = message.title
      ? message.title
      : "Server unable to process request";
    if (message.details) {
      if (Array.isArray(message.details)) {
        content = message.details;
      } else {
        content.push(message.details ? message.details : "");
      }
      notification.moreDetails = content;
    }
    this.notificationService.show(notification);
  }
  notifyGroup(error: any) {
    let notification = new NotificationProperties();
    notification.type = error.severity ? error.severity : "";
    notification.title = "List of errors";
    notification.userMessage = error.details ? error.details : "";
    if (error.contentArray) {
      notification.moreDetails = error.contentArray;
    }
    this.notificationService.show(notification);
  }
}
