import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import {
  BreadCrumbsModule,
  BreadCrumbsService,
  CalendarModule,
  CheckboxModule,
  HttpClientProgress,
  IdleSessionTrackerModule,
  NotificationModule,
  TreeCheckboxModule,
  SelectDropdownModule,
  AppSwitcherModule,
  HamburgerModule,
  HomePageHeaderModule,
  ModalModule,
  TooltipModule
} from "tgocp-ng/dist/";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { SystemErrorHandler } from './util/systemerror/system-error.handler';
import { SystemErrorModule } from './util/systemerror/system-error.module';
import { NotificationHandler } from "./util/exception/notfication.handler";

import { AppComponent } from './app.component';
import { AppRoutingModule } from "./app-routing.module";
import { UserInfoService } from './services/user-info.service';
import { MatomoModule } from 'ngx-matomo';
import { OnboardingService } from './services/onboarding.service';
import { DatePipe } from '@angular/common';
import { CustomFieldsService } from "./services/custom-fields.service";

import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { CompanyProfileService } from './services/company-profile.service';
import { APP_INITIALIZER } from '@angular/core';
import { AppInitService } from './services/app.init.service';
import { InvitedPartnerProfileService } from './services/invitedPartner-profile.service';
import { HeaderComponent } from './homepageHeader/homePageHeader.component';
import { HeaderService } from './services/header.service';
import { LogoutService } from './services/logout.service';
import { DialogueboxModule } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.component';
import { EditorModule } from 'tgocp-ng/dist/components/editor/editor.component';






export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, "./assets/i18n/", ".json");
}

export function initializeApp(appInitService: AppInitService, ps: NgxPermissionsService) {
  return (): Promise<any> => {
    return appInitService.load().then((data: any) => { return ps.loadPermissions(data.responseEntity) });
  }
}

@NgModule({
  declarations: [
    AppComponent,HeaderComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, MatomoModule, ModalModule,TreeCheckboxModule,
    BrowserAnimationsModule, SystemErrorModule, SelectDropdownModule, CalendarModule,
    NotificationModule, HttpClientProgress, BreadCrumbsModule, CheckboxModule, IdleSessionTrackerModule,
    HamburgerModule,AppSwitcherModule,HomePageHeaderModule,DialogueboxModule, TooltipModule,EditorModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (HttpLoaderFactory),
        deps: [HttpClient]
      }
    }),
    NgxPermissionsModule.forRoot(), AppRoutingModule
  ],
  providers: [
    { provide: ErrorHandler, useClass: SystemErrorHandler }, NotificationHandler,
    BreadCrumbsService, OnboardingService, UserInfoService, CompanyProfileService,
    AppInitService,InvitedPartnerProfileService,HeaderService,LogoutService,CustomFieldsService,
    DatePipe,
    { provide: APP_INITIALIZER, useFactory: initializeApp, deps: [AppInitService, NgxPermissionsService], multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
