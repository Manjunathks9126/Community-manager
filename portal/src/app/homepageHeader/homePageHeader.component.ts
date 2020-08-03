import { Component, ChangeDetectionStrategy, AfterContentInit, OnDestroy, HostListener } from '@angular/core';
import { HeaderService } from '../services/header.service';
import { HamburgerMenuType } from 'tgocp-ng/dist/components/hamburger/hamburger.menu.entity';
import { LogoutService } from '../services/logout.service';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { NotificationHandler } from '../util/exception/notfication.handler';

@Component({
    selector: 'homepage-header',
    templateUrl: './homePageHeader.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})

export class HeaderComponent implements OnDestroy {
    appList: any = [];
    menuData: HamburgerMenuType[] = [];
    showProfileModal: boolean = false;
    showDownloadModal: boolean = false;

    @HostListener('document:profileCloseEvent', ['$event', '$event.detail'])
    updateNodes(event, param) {
        this.showProfileModal = false;//(param!='close');
    }

    @HostListener('document:downloadsCloseEvent', ['$event', '$event.detail'])
    closeDownloadModal(event, param) {
        this.showDownloadModal = false;//(param!='close');
    }

    constructor(private headerService: HeaderService, private logoutService: LogoutService,
        private dialogueboxService: DialogueboxService, private notificationHandler: NotificationHandler) { }
    ngOnInit(): void {
        this.headerService.getHeaderFeatures().subscribe(
            (data) => {
                if (data && data.responseDetails.success == true) {
                    this.appList = data.responseDetails.responseEntity.appLauncherApps;
                    this.menuData = data.responseDetails.responseEntity.hamburgerMenu;
                }
            },
            (error) => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            }
        )
    }
    ngOnDestroy(): void {
    }
    signout() {
        this.logoutService.toggleLogoutConfirmPopup();
        this.dialogueboxService.confirm({
            dialogName: 'signout',
            accept: () => {
                this.logoutService.logout();
            }, reject: () => { }
        });
    }

    displaySignoutConfirm() {
        return this.logoutService.isLogoutConfirmVisible();
    }

}