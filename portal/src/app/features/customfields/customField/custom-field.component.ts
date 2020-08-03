import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from "@angular/core";
import { CustomFieldType, choice } from "../custom-field-type.entity";
import { CustomFieldGroupType } from "../custom-field-group-type.entity";
import { Constants } from '../../../services/AppConstant';
import { NgForm } from "@angular/forms";
import { CustomFieldsService } from "../../../services/custom-fields.service";
import { TranslateService } from "@ngx-translate/core";
import { NotificationHandler } from "../../../util/exception/notfication.handler";
import { DialogueboxService } from "tgocp-ng/dist/components/dialoguebox/dialoguebox.service";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import * as _ from 'lodash';

@Component({
  selector: 'custom-field',
  templateUrl: './custom-field.component.html'
})
export class CustomFieldComponent implements OnInit {

  scrollConfig = PSscrollUtils.scrollY();

  @Input() newField: CustomFieldType = new CustomFieldType();
  @Input() fieldType: string;
  @Input() selectedFieldGroupId: string;
  @Input() openModel: boolean = false;
  @Output() closeModal = new EventEmitter<any>();
  @ViewChild("fieldForm") fieldForm: NgForm;

  selectedChoices = [];
  sortAlphabatically: boolean;
  makeFirstDefault: boolean;
  customFieldMessages: any;
  //***************Field dependency********************* */
  fieldList: CustomFieldType[] = [];
  optionDependent = [];
  answerDependent: choice[] = []
  makeDependent: boolean = false;
  isConjDisabled: boolean = true;
  dependentCustomfieldId: any;
  currentLanguage: string = "en";
  customValues: any;
  customFieldIdEditFlag: boolean = false;
  selectedFieldType: any;
  isChoicesDirty: boolean = false;
  errorMessage: any;
  //*************Date Check**************** */
  selectDefaultDate: boolean = false;
  selectValidationRules: boolean = false;
  defaultAnswerchk: any;
  defaultAnswer: any;
  /***********File Download dependency */
  uploadedFiles: any = [];
  fileObjects: any = [];
  fileUploaded: any;
  validationMsgs: any;
  customFieldLabels: any;
  defaultMinValue: any;
  defaultMaxValue: any;
  defaultMaxLength: any;
  identifier = [];
  identifierRegex: "^[a-zA-Z0-9_]+$";
  id: any;
  validIdentifier: boolean = true;
  identifierPresent: boolean = false;

  constructor(private customFieldService: CustomFieldsService, private notificationHandler: NotificationHandler,
    private translate: TranslateService, private dialogueboxService: DialogueboxService) {

  }

  ngOnInit(): void {
    this.newField.customFieldType = this.fieldType;
    this.translate.get("customField.messages").subscribe(res => { this.customFieldMessages = res; });
    this.translate.get("customField.addFiled").subscribe(res => { this.customFieldLabels = res; });
    this.translate.get("customField.validationMsgs").subscribe(res => { this.validationMsgs = res; });

    this.currentLanguage = this.translate.currentLang;
    this.customValues = Constants;
    this.selectedFieldType = this.getCustomFieldData(this.fieldType);

    if (this.fieldType === 'Date' && this.newField.uniqueId) {
      this.initializeDateVariables();
    }

    if (this.selectedFieldType.showDefaultValue === 'true') {
      this.initializeValidationRules();
    }

    if (this.fieldType === 'File Download' && this.newField.uniqueId) {
      this.initializeFileDownloadVariables();
    }
    //***************Field dependency********************* */
    if (this.newField.dependentCustomfieldId) {
      this.makeDependent = true;//set check box value
      this.getAllFields();
    }
    this.customFieldIdEditFlag = this.customFieldService.checkIfFieldIdPresent(this.newField.customFieldId);
    //***************Field dependency********************* */
  }

  ngAfterViewInit() {
    this.customFieldService.intializePristineForm(this.fieldForm);
  }

  initializeDateVariables() {
    if (this.newField.defaultAnswer && this.newField.validationRules !== "current_date") {
      this.defaultAnswerchk = "select_date";
      this.defaultAnswer = new Date(this.newField.defaultAnswer)
      this.selectDefaultDate = true;
    } else {
      if (this.newField.validationRules && this.newField.validationRules == "current_date") {
        this.defaultAnswerchk = "current_date";
        this.selectDefaultDate = true;
      }
    }

  }

  initializeValidationRules() {
    if (this.newField.validationRules) {
      var validationRules = JSON.parse(this.newField.validationRules);
      this.defaultMinValue = validationRules.defaultMinValue;
      this.defaultMaxValue = validationRules.defaultMaxValue;
      this.defaultMaxLength = validationRules.defaultMaxLength;
    }
  }

  initializeFileDownloadVariables() {
    if (this.newField && this.newField['fileObjects']) {
      this.fileObjects = this.newField['fileObjects']
    }
  }

  addField() {
    this.identifier = [] = [];
    this.newField.groupId = this.selectedFieldGroupId;
    //***************Field dependency********************* */
    this.newField.dependentCustomfieldId = _.cloneDeep(this.dependentCustomfieldId);
    if (!this.makeDependent) {
      this.newField.dependentCustomfieldId = "";
      this.newField.dependencyConjunction = "OR";
      this.newField.dependentChoiceIds = [];
    }

    if (this.selectedFieldType.showDefaultValue === 'true') {
      this.newField.validationRules = {};
      if (this.newField.customFieldType === 'Number') {
        this.newField.validationRules.defaultMinValue = this.defaultMinValue;
        this.newField.validationRules.defaultMaxValue = this.defaultMaxValue;
      }
      this.newField.validationRules.defaultMaxLength = this.defaultMaxLength;
      this.newField.validationRules = JSON.stringify(this.newField.validationRules);
    }
    if (this.fieldType === 'Date') {
      this.setDefaultDateValidation();
    }

    if (this.selectedFieldType.showChoices === 'true') {
      this.identifierValidation();
      this.newField.choices = (this.newField.choices || []).filter(choice => (choice.id || (!choice.id && choice.value)));
      if (this.newField.choices.length > 0) {
        let choices = [...this.newField.choices];

        if (this.sortAlphabatically) {
          choices = this.sort(choices, 'value')
        }
        this.newField.choices = this.repositionChoices(choices);

        this.newField.defaultAnswer = null;
        if (this.makeFirstDefault) {
          this.newField.defaultChoice = this.newField.choices[0];
        } else if (this.newField.defaultChoice && this.newField.defaultChoice.id) {
          this.newField.defaultChoice = this.newField.choices.find(i => i.id == this.newField.defaultChoice.id);
        }

      } else {
        this.translate.get(this.selectedFieldType.warningMessage).subscribe(res => { this.errorMessage = res; })
        this.notificationHandler.notify({ severity: 'warning', title: this.errorMessage });
        return;
      }
      this.isChoicesDirty = false;

    } else {
      this.newField.choices = [];
      this.newField.defaultChoice = null;
    }

    if (this.fieldType === 'File Download') {
      this.setFileObject();
    }
    this.closeModal.emit(this.newField);
  }

  closeFieldModal() {
    this.fieldForm.reset();
    this.isChoicesDirty = false;
    this.closeModal.emit(false);
  }

  //to get the dependent field options
  getAllFields() {

    if (this.makeDependent && !(this.optionDependent.length > 0)) {
      this.optionDependent = [];
      let type = 'Drop-down,Multiple Choice,Radio Button'
      this.customFieldService.getfieldListForFieldDependency(this.newField.uniqueId, this.selectedFieldGroupId, type).subscribe(
        data => {
          if (data['responseDetails']['success'] && data['responseDetails']['responseEntity'].length > 0) {
            this.fieldList = data['responseDetails']['responseEntity'];
            this.fieldList.forEach(field => {
              if (field.uniqueId != this.newField.uniqueId)
                this.optionDependent.push({ "value": field.uniqueId, "label": field.question })
            })

            //get the choices on Init
            if (this.newField.dependentCustomfieldId) {
              this.onDependentFieldSelection(this.newField.dependentCustomfieldId, '');
              //to display the dependent Field name in DropDown box
              this.dependentCustomfieldId = _.cloneDeep(this.newField.dependentCustomfieldId);
            }

          } else {
            this.makeDependent = false;
            this.notificationHandler.notify({ severity: 'warning', title: this.customFieldMessages.notificationTitle.noField });

          }
        }, error => {
          this.makeDependent = false;
          this.notificationHandler.notify({ severity: 'error', title: error.userMessage });
        });
    }
  }

  /*call to get Answers for selected Question(i.e. dependent field)
  *and to check type of selected question
  */
  onDependentFieldSelection(uniqueId, actionType: string) {
    if (actionType === 'change') {
      this.newField.dependentChoiceIds = [];
      this.newField.dependencyConjunction = 'OR';
    }
    this.getAnswers(uniqueId);
    this.checkSelectedFieldType(uniqueId);
  }

  //check the type of selected dependent field(question)
  checkSelectedFieldType(uniqueId) {
    this.isConjDisabled = false;
    this.fieldList.forEach(field => {
      if (field.uniqueId === uniqueId && (field.customFieldType === 'Drop-down' || field.customFieldType === 'Radio Button')) {
        this.isConjDisabled = true;
      }
    })
  }
  //get the answers for selected dependent field(question)
  getAnswers(uniqueId) {
    this.answerDependent = [];
    this.fieldList.forEach(option => {
      if (option.uniqueId === uniqueId && option.choices) {
        this.answerDependent = [...this.answerDependent, ...option.choices];
      }
    })
  }
  getCustomFieldData(fieldType) {
    let newFieldData = {
      "Text": {
        displayIcon: "ot-icon-text",
        displayText: this.customFieldLabels.text,
        showDefaultValue: 'true',
        maxLength: 120,
        displayLabel: 'false'
      },
      "Text Area": {
        displayIcon: "ot-icon-text",
        displayText: this.customFieldLabels.text,
        showDefaultValue: 'true',
        maxLength: 4000,
        displayLabel: 'false'
      },
      "Number": {
        displayIcon: "ot-icon-number",
        displayText: this.customFieldLabels.number,
        showDefaultValue: 'true',
        maxLength: 15
      },
      "Multiple Choice": {
        displayIcon: "ot-icon-multichoice",
        displayText: this.customFieldLabels.multiChoice,
        showChoices: 'true',
        CHOICE_LIMIT: 300,
        warningMessage: "At least one (1) choice is required"
      },
      "Radio Button": {
        displayIcon: "ot-icon-radiobutton",
        displayText: this.customFieldLabels.radioButton,
        showChoices: 'true',
        CHOICE_LIMIT: 7,
        warningMessage: "Add choices for Radio Button field"
      },
      "Drop-down": {
        displayIcon: "ot-icon-dropdown",
        displayText: this.customFieldLabels.dropdown,
        showChoices: 'true',
        CHOICE_LIMIT: 300,
        warningMessage: "At least one (1) choice is required"
      },
      "Date": {
        displayIcon: "ot-icon-date",
        displayText: this.customFieldLabels.date,
      },
      "File Upload": {
        displayIcon: "ot-icon-file-upload",
        displayText: this.customFieldLabels.file_upload
      },
      "File Download": {
        displayIcon: "ot-icon-file-download",
        displayText: this.customFieldLabels.file_download
      }
    }
    return newFieldData[fieldType];
  }

  addChoice() {
    if (this.newField.choices == null) {
      this.newField.choices = [];
    }
    this.newField.choices.push(new choice());
    this.isChoicesDirty = true;
  }

  deleteChoice() {
    this.dialogueboxService.confirm({
      dialogName: 'deleteChoice',
      accept: () => {
        for (let selectedChoice of this.selectedChoices) {
          if (selectedChoice.id) {
            this.newField.choices = this.newField.choices.filter(choice => choice.id != selectedChoice.id);
            if (this.newField.defaultChoice && this.newField.defaultChoice.id == selectedChoice.id) this.newField.defaultChoice = null;
          } else
            this.newField.choices = this.newField.choices.filter(choice => choice.value != selectedChoice.value);
        }
        //this.newField.choices = this.repositionChoices( [...this.newField.choices]);
        this.isChoicesDirty = true;
        this.selectedChoices = [];
      }
    });
  }

  private repositionChoices(choices: Array<choice>): Array<choice> {
    let position = 0;
    for (let choice of choices) {
      choice.position = ++position;
    }
    return choices;
  }


  private sort(data: Array<any>, sortBy): Array<any> {
    return (data || []).sort((a: any, b: any) => a[sortBy] < b[sortBy] ? -1 : 1);
  }

  setDefaultDateValidation() {
    if (this.selectDefaultDate) {
      if (this.defaultAnswerchk == "current_date") {
        this.newField.validationRules = "current_date";
        this.newField.defaultAnswer = null;
      } else {
        if (this.defaultAnswer) {
          this.newField.defaultAnswer = this.defaultAnswer.getMonth() + 1 + "/" + this.defaultAnswer.getDate() + "/" + this.defaultAnswer.getFullYear();
          this.newField.validationRules = "";
        }
        else
          this.newField.defaultAnswer = null;
      }
    } else {
      this.newField.defaultAnswer = null;
      this.newField.validationRules = "";
    }

  }

  attachFile(event, fileObjects) {
    if (event.target.files && event.target.files[0]) {
      if (event.target.files[0].size === 0) {
        this.notificationHandler.notify({ severity: 'warning', title: this.validationMsgs.emptyFiles })
      } else if (this.fileObjects.length > 4) {
        this.notificationHandler.notify({ severity: 'warning', title: this.validationMsgs.maxFiles })
      } else if (event.target.files[0].type === "application/x-msdownload") {
        this.notificationHandler.notify({ severity: 'warning', title: this.validationMsgs.fileType })
      } else if (event.target.files[0].size / 1024 / 1024 > 25) {
        this.notificationHandler.notify({ severity: 'warning', title: this.validationMsgs.fileSize })
      } else if (event.target.files[0].name.length > 120) {
        this.notificationHandler.notify({ severity: 'warning', title: this.validationMsgs.fileName })
      } else {
        var reader = new FileReader();
        reader.onload = e => {
          var arrayBuffer = e.target['result'];
          var fileType = event.target.files[0].name.substr(event.target.files[0].name.lastIndexOf('.') + 1, event.target.files[0].name.length);
          this.fileObjects.push({ fileName: event.target.files[0].name, fileType: fileType, fileData: arrayBuffer });
        }
        reader.readAsDataURL(event.target.files[0]);
      }
    }
  }

  clearSchemaFile(index) {
    this.fileObjects.splice(index, 1);
  }

  setFileObject() {
    this.newField['fileObjects'] = this.fileObjects;

    this.newField['fileObjects'].forEach(
      file => {
        if (file.fileData) {
          let fileString = file.fileData.split(",")
          file.fileData = fileString[1];
        }
      })
  }

  download(fileId, fileName) {
    window.open(this.customFieldService.download(fileId, fileName), '_self');
  }

  checkCharacterLength(max) {

    if (max < 1) {
      this.fieldForm.controls['defaultMaxLength'].setErrors({ 'maxLength': true });
      return 'Max length should be greater than 1';
    } else if (max > 120) {
      this.fieldForm.controls['defaultMaxLength'].setErrors({ 'maxLength': true });
      return 'Max length should be less than 120';
    } else {
      return '';
    }

  }

  checkNumberRange(min, max) {
    if (min && max) {
      if (+max <= +min) {
        return 'Max value must be greater than min value';
      }
    }

    if (min === '') {
      this.fieldForm.controls['defaultMaximumValue'].setErrors(null);
      return '';
    } else {
      if (!max || max === '') {
        this.fieldForm.controls['defaultMaximumValue'].setErrors({ 'required': true });
        return 'Max value is required';
      } else {
        return '';
      }

    }


  }
  identifierValidation() {
    this.newField.choices.forEach(choice => {
      let tobeshown = true;
      this.identifier.forEach(identifier => {
        if (tobeshown) {
          if (identifier.value == choice.choiceIdentifier) {
            if (tobeshown) {
              this.id = identifier.position;
              tobeshown = false;
            }
            let id = 'number' + this.id;
            if (this.customFieldIdEditFlag) {
              document.getElementById(id).focus();
            }
            this.newField.isIdentifierUnique = false;
            this.notificationHandler.notify({ severity: 'warning', title: this.customFieldMessages.warning.identifierValidation });
          }
          else {
            this.newField.isIdentifierUnique = true;
          }
        }

      })
      this.identifier.push({ "value": choice.choiceIdentifier, "position": choice.position });
    })
  }

  checkError(inputVal) {
    var format = /^[a-zA-Z0-9-_ ]+$/;
    if (inputVal.match(format)) {
      this.validIdentifier = true;
    } else {
      this.validIdentifier = false;
      return this.validationMsgs.identifier;
    }

  }


  checkDefaultValue(defaultValue, defaultMaxLength, minValue, maxValue) {
    if (defaultMaxLength) {
      if (defaultValue.length > defaultMaxLength) {
        this.fieldForm.controls['defaultValue'].setErrors({ 'maxLength': true });
        return 'Default value must not exceed Max length';
      } else {
        this.fieldForm.controls['defaultValue'].setErrors(null);
        return '';
      }
    }

    if (minValue && maxValue) {
      if (+defaultValue < +minValue) {
        this.fieldForm.controls['defaultValue'].setErrors({ 'minValue': true });
        return 'Default value must be greater than ' + minValue;
      } else if (+defaultValue > +maxValue) {
        this.fieldForm.controls['defaultValue'].setErrors({ 'maxValue': true });
        return 'Default value must be less than ' + maxValue;
      } else {
        return '';
      }

    }
  }

}
