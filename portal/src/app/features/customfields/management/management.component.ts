import { Component, OnInit, ViewChild, HostListener } from '@angular/core';
import { NgForm } from '@angular/forms';
import { CustomFieldsService } from '../../../services/custom-fields.service';
import { NotificationHandler } from '../../../util/exception/notfication.handler';
import { CustomFieldGroupType } from '../custom-field-group-type.entity';
import { CustomFieldType, choice } from '../custom-field-type.entity';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { PSscrollUtils } from 'tgocp-ng/dist/shared/perfect-scrollbar-config';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { Constants } from '../../../services/AppConstant';

@Component({
  templateUrl: './management.component.html'
})
export class ManagementComponent implements OnInit {

  scrollConfig = PSscrollUtils.scrollY();

  constructor(private customFieldService: CustomFieldsService, private dialogueboxService: DialogueboxService,
    private notificationHandler: NotificationHandler, private translate: TranslateService) { }

  FieldGroups: any[] = [];
  selectTab: number = 0;
  selectedFieldGroup: CustomFieldGroupType;
  fieldList: CustomFieldType[] = [];
  fieldGroupModal: boolean = false;
  newFieldGroup: any = {};
  updatedGroupAndRoles: any = {};
  groupAndRoles: any = {};
  newField: CustomFieldType = new CustomFieldType();
  @ViewChild("groupForm") groupForm: NgForm;
  actionSource: string = null;
  selectedData = [];
  fieldType: string;
  customMessages: any;
  userRoles: any[] = [];
  usersAvailable: any[] = [];
  /* ******************Dependency Field******************************* */
  fieldListOfBU: CustomFieldType[] = [];
  optionDependent = [];
  answerDependent: choice[] = []
  makeDependentChkBox: boolean = false;
  isConjDisabled: boolean = true;
  dependentQuestionId: any;
  clicked: boolean = false;
  showTip: boolean = false;
  selectedRoles: any[] = [];
  grpFieldRoles: any[];
  selectedGroupRoles: any[] = [];
  toChecked: boolean = true;
  permissionTypes: any;
  perm: any[] = [];
  onlyView: boolean = false;
  rolesCount: number = 0;
  checkedRoles: any[] = [];
  rolesModified: boolean = false;
  correctRoles: boolean = false;
  fetchCustomRoles: boolean = false;
  rolesChoosen: any[] = [];
  validDescriptionLength: boolean = true;
  tooltipDescription: any;
  identifierFlag: boolean;
  defaultCategory: any;

  /* ******************************************************** */
  currentLanguage: string = "en";
  ngOnInit() {
    this.permissionTypes = Constants;
    this.initializeData();
    this.getRolesList();
    this.translate.get("customField.messages").subscribe(res => { this.customMessages = res; })
    this.currentLanguage = this.translate.currentLang
  }
  private initializeData() {
    this.customFieldService.getDefaultCategory().subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.defaultCategory = data['responseDetails']['responseEntity'].categoryId;
        }
      }, error => {
        if (error.status != 404)
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
      });
    this.customFieldService.getCustomFieldGroup(false).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.FieldGroups = data['responseDetails']['responseEntity'].itemList;
          if (this.FieldGroups && this.FieldGroups.length > 0) {
            if (this.actionSource && this.actionSource === "N") {
              this.actionSource = null;
              this.selectTab = this.FieldGroups.length - 1;
            }
            this.selectGroup(this.FieldGroups[this.selectTab], this.selectTab)
          }
        }
      }, error => {
        if (error.status != 404)
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
      });
  }

  selectGroup(data, position) {
    this.fieldList = [];
    this.selectTab = position;
    if (data == null) return;
    this.selectedFieldGroup = new CustomFieldGroupType(data.uniqueId, data.name, data.description, data.ownerBusinessUnit.buId, '', [], '', data.categoryId);
    let temp: any;
    temp = document.createElement("div");
    temp.innerHTML = this.selectedFieldGroup.description;
    this.tooltipDescription = temp.textContent || temp.innerText || "";
    this.loadFields(data.uniqueId);
  }

  loadFields(groupId) {
    this.customFieldService.getCustomField(groupId, '').subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.fieldList = data['responseDetails']['responseEntity'];
        }
      }, error => {
        if (error.status != 404)
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
      });

  }
  isDependent(data) {
    if (data.dependentCustomfieldId)//&& data.dependentChoiceIds && data.dependentChoiceIds.length>0)
      return true;
    else
      return false;
  }
  getDependentName(data) {
    let question = "";
    if (data.dependentCustomfieldId) {// && data.dependentChoiceIds && data.dependentChoiceIds.length>0){
      question = this.fieldList.find(field => field.uniqueId == data.dependentCustomfieldId).question;
    }
    return question;
  }

  closeFieldModal(value: any) {
    this.fieldType = '';
    this.newField = new CustomFieldType();
    if (value) {
      this.loadFields(this.selectedFieldGroup.uniqueId);
    }
  }

  saveFieldGroup() {
    /* *******************Dependency field*********************************** */
    this.newFieldGroup.dependentCustomfieldId = _.cloneDeep(this.dependentQuestionId);
    this.correctRoles = this.validateRoles();
    if (!this.makeDependentChkBox) {
      this.newFieldGroup.dependentCustomfieldId = "";
      this.newFieldGroup.dependencyConjunction = "OR";
      this.newFieldGroup.dependentChoiceIds = [];
    }
    /* ****************************************************** */
    this.updatedGroupAndRoles.fgData = this.newFieldGroup;
    this.updatedGroupAndRoles.selectedRoles = this.grpFieldRoles;
    if (this.validateRoles()) {
      this.checkedRoles = [];
      this.rolesChoosen = [];
      if (!this.newFieldGroup['uniqueId']) {
        this.customFieldService.addCustomFieldGroup(this.updatedGroupAndRoles).subscribe(
          data => {
            this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
            this.actionSource = "N";
            this.initializeData();
            this.closeFieldGroupModal();
          },
          error => {
            if (error.appErrorCode === "CUSTOM_GROUP_MAX_LIMIT_400") {
              let userMessage = this.customMessages.failure.maxLimit.replace("{0}", this.FieldGroups.length);
              this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.createGroupFail, details: userMessage })
            } else {
              this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.createGroupFail, details: error.userMessage })
            }
          }
        );
      } else {
        this.customFieldService.ediCustomFieldGroup(this.updatedGroupAndRoles).subscribe(
          data => {
            this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
            this.initializeData();
            this.closeFieldGroupModal();
          },
          error => this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.updateGroupFail, details: error.userMessage })
        );

      }
    }
  }

  deleteField(id?: string) {
    let ids = [];
    if (id && id.length > 1) {
      ids.push(id);
    } else {
      this.selectedData.forEach(
        data => ids.push(data.uniqueId)
      )
    }
    if (ids.length > 0) {
      this.dialogueboxService.confirm({
        dialogName: 'deleteField',
        accept: () => {
          this.customFieldService.deleteFields(ids).subscribe(
            success => {
              this.notificationHandler.notify({ severity: 'success', title: success['responseDetails']['statusMessage'] });//'Field(s) deleted successfully'
              this.loadFields(this.selectedFieldGroup.uniqueId);
              this.selectedData = [];
            },
            error => this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.deleteFail, details: error.userMessage })
          );
        }
        //, reject: () => { }
      });

    }
  }

  edit(inputData: CustomFieldType) {
    this.newField = JSON.parse(JSON.stringify(inputData));//Object.assign({}, inputData);
    this.fieldType = inputData.customFieldType;
  }

  more(event: Event) {
    event.preventDefault();
    event.stopPropagation();
  }

  closeFieldGroupModal() {
    this.groupForm.reset();
    this.newFieldGroup = {};
    this.fieldGroupModal = false;
    //***************Field dependency********************* */
    this.makeDependentChkBox = false;
    this.dependentQuestionId = '';
    this.optionDependent = [];
    this.answerDependent = [];
    //***************Field dependency********************* */
  }

  addGroup() {
    this.newFieldGroup = new CustomFieldGroupType(null, null, null, null, null, [], null, this.defaultCategory);
    this.fieldGroupModal = true;
    this.userRoles.forEach(
      data => data.oldValue = false
    );
    this.selectedRoles = [];
    this.getSelectedRolesId(this.selectedRoles);
  }
  editGroup(inputData: CustomFieldGroupType, position: number) {
    //this.selectTab=position;
    this.checkedRoles = [];
    this.rolesChoosen = [];
    this.customFieldService.rolesCustomFieldGroup(inputData.uniqueId).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.groupAndRoles = data['responseDetails']['responseEntity'];
          this.newFieldGroup = this.groupAndRoles.fgData;
          this.fieldGroupModal = true;
          this.toChecked = false;
          for (let i = 0; i < this.userRoles.length; i++) {
            this.userRoles[i].oldValue = false;
            this.userRoles[i].newValue = false;
          }
          this.selectedRoles = this.groupAndRoles.selectedRoles;
          this.rolesCount = this.selectedRoles.length;
          this.getSelectedRolesId(this.selectedRoles);
          this.checkedRoles = this.rolesChoosen;
          this.rolesModified = false;

          //***************Field dependency********************* */
          this.makeDependentChkBox = false;
          this.answerDependent = [];
          if (this.newFieldGroup.dependentCustomfieldId) {//&& this.newFieldGroup.dependentChoiceIds && this.newFieldGroup.dependentChoiceIds.length>0){
            this.dependentQuestionId = '';
            this.makeDependentChkBox = true;//set check box value
            this.getQuestionsForgroup();
            //***************Field dependency********************* */
          }
        }
      }, error => {
        if (error.status != 404)
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
      });


  }

  deleteFieldGroup(id: string) {
    if (id != null && id.length > 0) {
      this.dialogueboxService.confirm({
        dialogName: 'deleteFieldGroup',
        accept: () => {
          this.customFieldService.deleteCustomFieldGroup(id).subscribe(
            success => {
              this.notificationHandler.notify({ severity: 'success', title: success['responseDetails']['statusMessage'] });
              this.initializeData();
            },
            error => this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.deleteFail, details: error.userMessage })
          );
        }
        //, reject: () => { }
      });

    }
  }

  /****************Dependency Field************************ */

  getQuestionsForgroup() {

    if (this.makeDependentChkBox && !(this.optionDependent.length > 0)) {
      this.optionDependent = [];
      let type = 'Drop-down,Multiple Choice,Radio Button'
      this.customFieldService.getfieldListForGroupDependency(this.newFieldGroup.uniqueId, type).subscribe(
        data => {
          if (data['responseDetails']['success'] && data['responseDetails']['responseEntity'] && data['responseDetails']['responseEntity'].length > 0) {
            this.fieldListOfBU = data['responseDetails']['responseEntity'];
            this.fieldListOfBU.forEach(field => {
              if (field.uniqueId != this.newField.uniqueId)
                this.optionDependent.push({ "value": field.uniqueId, "label": field.question })
            })
            //get the choices on Init
            if (this.newFieldGroup.dependentCustomfieldId) {
              this.onDependentFieldSelection(this.newFieldGroup.dependentCustomfieldId, '');
            }
            //to display the dependent Field name in DropDown box
            if (this.newFieldGroup.dependentCustomfieldId) {//&& this.newFieldGroup.dependentChoiceIds && this.newFieldGroup.dependentChoiceIds.length>0){
              this.dependentQuestionId = _.cloneDeep(this.newFieldGroup.dependentCustomfieldId);
            }

          } else {
            this.makeDependentChkBox = false;
            this.notificationHandler.notify({ severity: 'warning', title: this.customMessages.notificationTitle.noField });

          }
        }, error => {
          this.makeDependentChkBox = false;
          if (error.appErrorCode == 'TWO_LEVEL_DEPENDENCY_400')
            this.notificationHandler.notify({ severity: 'error', title: error.userMessage });
          else
            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    }
  }

  /*call to get Answers for selected Question(i.e. dependent field)
      *and to check type of selected question
      */
  onDependentFieldSelection(uniqueId, actionType) {
    if (actionType == 'change') {
      this.newFieldGroup.dependentCustomfieldId = _.cloneDeep(this.dependentQuestionId);
      this.newFieldGroup.dependentChoiceIds = [];
      this.newFieldGroup.dependencyConjunction = 'OR';
    }
    this.getAnswers(uniqueId);
    this.checkSelectedFieldType(uniqueId);
  }

  //check the type of selected dependent field(question)
  checkSelectedFieldType(uniqueId) {
    this.isConjDisabled = false;
    this.fieldListOfBU.forEach(field => {
      if (field.uniqueId == uniqueId && (field.customFieldType == 'Drop-down' || field.customFieldType == 'Radio Button')) {
        this.isConjDisabled = true;
      }
    })
  }
  //get the answers for selected dependent field(question)
  getAnswers(uniqueId) {
    this.answerDependent = [];
    this.fieldListOfBU.forEach(option => {
      if (option.uniqueId === uniqueId && option.choices) {
        this.answerDependent = [...this.answerDependent, ...option.choices];
      }
    })
  }

  // get the list of roles associated with particular field group
  private getRolesList() {
    this.customFieldService.getAvailableRoles().subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.userRoles = data['responseDetails']['responseEntity'];
        }
        if (this.userRoles.length) {
          this.fetchCustomRoles = true;
        }
      }, error => {
        if (error.status != 404)
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
      });

  }

  getUsers(userId, event) {
    this.clicked = !this.clicked;
    this.showTip = true;
    if (this.clicked) {
      this.customFieldService.getListOfUsers(userId).subscribe(
        data => {
          if (data['responseDetails']['success']) {
            this.usersAvailable = data['responseDetails']['responseEntity'];
            if (this.usersAvailable.length === 0) {
              this.usersAvailable = this.customMessages.failure.roleFailure.noUsers;
            }
          }
        }, error => {
          if (error.status != 404)
            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    }
  }

  @HostListener('document:click', ['$event']) clickedOutside($event) {
    // here you can hide your menu
    this.showTip = false;
  }

  checkedValue($event, role) {
    this.rolesModified = true;
    let index = this.checkedRoles.indexOf(role);
    for (let neoUserroles of this.grpFieldRoles) {
      if (role.uniqueId === neoUserroles.uniqueId) {
        neoUserroles.roleId = role.uniqueId;
        if ($event == false) {
          neoUserroles.newValue = false;
          this.rolesCount = this.rolesCount - 1;
          this.checkedRoles.splice(index, 1);
          break;
        }
        else if ($event == true) {
          neoUserroles.newValue = true;
          this.rolesCount = this.rolesCount + 1;
          this.checkedRoles.push(role);
        }
      }
    }
  }

  getSelectedRolesId(selectedRoles) {
    for (let assignedRoles of selectedRoles) {
      for (let roles of this.userRoles) {
        if (roles.uniqueId == assignedRoles.roleId) {
          roles.oldValue = true;
          roles.newValue = true;
          roles.roleId = roles.uniqueId;
          this.rolesChoosen.push(roles);
          break;
        }
      }
    }
    this.grpFieldRoles = _.cloneDeep(this.userRoles);
  }

  getPermissions(roleQualifier, event) {
    this.perm = this.permissionTypes.edit_permissions;
    if (_.isEqual(roleQualifier, this.permissionTypes.VIEW_CFG)) {
      this.perm = this.permissionTypes.view_permissions;
    }
  }

  validateRoles() {
    if (!this.makeDependentChkBox && this.checkedRoles.length > 0) {
      if (this.rolesCount > 30) {
        this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.updateGroupFail, details: this.customMessages.failure.roleFailure.limit })
        return false;
      }
      let validRoles = this.checkedRoles.some(r => r.roleQualifier === this.permissionTypes.EDIT_CFG);
      if (!validRoles) {
        this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.updateGroupFail, details: this.customMessages.failure.roleFailure.edit })
        return false;
      }
    }
    return true;
  }
  /***************************************************** */
  textChange(event) {
    if (event.htmlValue && event.htmlValue.length > 4000) {
      this.validDescriptionLength = false;
    }
    else {
      this.validDescriptionLength = true;
    }
  }

  /* Add or Update */
  addOrEditCustomField(newField: any) {
    if (this.newField.choices && this.newField.choices.length > 0) {
      this.newField.choices.forEach(choice => {
        if (choice.value && !choice.choiceIdentifier) {
          this.notificationHandler.notify({ severity: 'warning', title: this.customMessages.warning.identifier });
          this.identifierFlag = false;
        }
        else {
          this.identifierFlag = true;
        }
      })
    }
    if (!newField) {
      this.closeFieldModal(false);
    }
    else if (this.newField.choices && this.newField.choices.length > 0 && !this.newField.isIdentifierUnique) {
      return;
    }
    else if (this.newField.choices && this.newField.choices.length > 0 && !this.identifierFlag) {
      return;
    }
    else {
      this.newField.customFieldType = this.fieldType;
      if (newField.uniqueId == null) {

        this.customFieldService.addCustomField(newField).subscribe(
          data => {
            this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
            this.closeFieldModal(true);
          },
          error => {
            if (error.appErrorCode === "CUSTOM_FIELD_MAX_LIMIT_400") {
              let userMessage = this.customMessages.failure.fieldMaxLimit.replace("{0}", this.fieldList.length);
              this.notificationHandler.notify({ severity: 'error', title: this.customMessages.notificationTitle.createFieldFail, details: userMessage })
            } else {
              this.notificationHandler.notify({ severity: 'error', title: error.userMessage })
            }
          }
        );
      } else {
        this.customFieldService.editCustomField(newField).subscribe(
          data => {
            this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
            this.closeFieldModal(true);
          },
          error => this.notificationHandler.notify({ severity: 'error', title: error.userMessage })
        );
      }
    }
  }
}
