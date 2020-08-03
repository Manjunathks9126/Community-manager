import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpEventType, HttpParams } from '@angular/common/http';
import { ExceptionWrapper } from '../util/exception/exception-wrapper';
import { catchError, map } from "rxjs/operators";
import { Observable } from 'rxjs';

@Injectable()
export class CustomFieldsService {

  constructor(private http: HttpClient) { }
  getCustomfDataFieldGroup(companyId) {
    return this.http.get("customdata/fieldGroup/list" + "?partnerBuId=" + companyId)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  getCustomFieldGroup(parentsOnly: boolean) {
    return this.http.post("customfieldgroups/list?sortBy=creationDate" + (parentsOnly ? "&parentsOnly=true" : ""), [])
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  getFieldGroup(groupId) {
    let query = "";
    if (groupId) {
      query = "&groupId=" + groupId;
    }
    return this.http.post("customfieldgroups/list" + "?sortBy=creationDate" + query, [])
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  addCustomFieldGroup(data) {
    return this.http.post("customfieldgroups/create", data)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  ediCustomFieldGroup(newFieldGroup: any): any {
    return this.http.post("customfieldgroups/edit", newFieldGroup)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  deleteCustomFieldGroup(id) {
    return this.http.get("customfieldgroups/delete/" + id)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  rolesCustomFieldGroup(id) {
    return this.http.get("customfieldgroups/roles/" + id)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  getCustomField(groupId: string, type) {
    return this.http.get("customfields/list?forGroupId=" + groupId + "&type=" + type + "&sortBy=creationDate")
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  getfieldListForFieldDependency(fieldId, groupId, type) {
    let query = "";
    if (fieldId) {
      query = "?fieldId=" + fieldId + "&forGroupId=" + groupId + "&type=" + type + "&sortBy=creationDate";
    } else {
      query = "?forGroupId=" + groupId + "&type=" + type + "&sortBy=creationDate";
    }
    return this.http.get("customfields/listForFieldDependency" + query)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  getfieldListForGroupDependency(groupId, type) {
    let query = "";
    if (groupId) {
      query = "?forGroupId=" + groupId + "&type=" + type + "&sortBy=creationDate";
    } else {
      query = "?type=" + type + "&sortBy=creationDate";
    }
    return this.http.get("customfields/listForGroupDependency" + query)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  addCustomField(data) {
    return this.http.post("customfields/create", data)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  editCustomField(data) {
    return this.http.post("customfields/edit", data)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  deleteFields(ids: string[]) {
    return this.http.post("customfields/delete", ids)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  getCustomFieldWithAnswer(groupId, partnerBuid): Observable<any> {
    return this.http.get("customdata/partner/withAnswer?groupId=" + groupId + "&partnerBuId=" + partnerBuid)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  updateCustomFieldWithAnswer(partnerBuid, data) {
    return this.http.post("customdata/partner/withAnswer?partnerBuId=" + partnerBuid, data)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  getAvailableRoles() {
    return this.http.get("customRoles")
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  getListOfUsers(userId) {
    return this.http.get("customRoles/" + userId + "/users")
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
  download(fileId: string, fileName: string) {
    return "customfields/" + fileId + "/download?fileName=" + fileName;
  }
  intializePristineForm(fieldForm) {
    setTimeout(() => {
      for (const key in fieldForm.controls) {
        if (fieldForm.controls[key]) {
          fieldForm.controls[key].markAsPristine();
        }
      }
    });
  }

  checkIfFieldIdPresent(fieldId){
    if(fieldId){
      return false;
    }else{
     return true;
    } 
  }

  private getEventMessage(event: HttpEvent<any>, formData) {
    switch (event.type) {

        case HttpEventType.UploadProgress:
            return this.fileUploadProgress(event);

        case HttpEventType.Response:
            return this.apiResponse(event);

        /*  default:
             return `File "${formData.get('file').name}" surprising upload event: ${event.type}.`; */
    }

  }

  private fileUploadProgress(event) {
    const percentDone = Math.round(100 * event.loaded / event.total);
    return { status: 'progress', message: percentDone };
  }

  private apiResponse(event) {
    return { status: 'completed', message: event.body.responseDetails.responseEntity };
  }

  bulkCustomImportApi = "bulk/customFields";

    uploadCustomImportFile(formData) {
        return this.http.post<any>(this.bulkCustomImportApi + "/validate", formData, {
            reportProgress: true,
            observe: 'events'
        }).pipe(
            map(event => this.getEventMessage(event, formData)),
            catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    downloadCustomImportTemplate(template) {
      return this.bulkCustomImportApi + "/template?templateName=" + template;
    }

    submitCustomImportUploadedFile(formData: FormData) {
      return this.http.post(this.bulkCustomImportApi + "/submitfile", formData)
          .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
}
