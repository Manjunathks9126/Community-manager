import { Component, Input } from '@angular/core';

@Component({
    selector: 'json-tree',
    template: `
        <ng-template #recursiveTree let-node>        
            <ot-dataList [value]="getKeys(node)">
                <ng-template let-key pTemplate="item">
                <div *ngIf="isNotEmptyNode(node[key])">
                    <span class="attribute-label">{{key}} :</span>
                    <span *ngIf="!isObject(node[key])" class="attribute-value">{{node[key]}}</span>
                </div>
                <ul *ngIf="isNotEmptyObject(node[key])">
                    <ng-container *ngTemplateOutlet="recursiveTree; context:{ $implicit: node[key] }"></ng-container>
                </ul>
                </ng-template>
            </ot-dataList>        
        </ng-template>
        <ng-container *ngTemplateOutlet="recursiveTree; context:{ $implicit: jsonNode }"></ng-container>
  `
})
export class JsonTree {
    @Input() jsonNode;

    isObject(obj) {
        if (obj instanceof String || typeof obj === "string") {
            return false;
        }
        return (obj && (Object.keys(obj).length > 0));
    }

    isNotEmptyObject(obj) {
        return this.isObject(obj) && Object.keys(obj).some(key => this.isObject(obj[key]) ?
            this.isNotEmptyObject(obj[key]) : (Array.isArray(obj[key]) ? obj[key].length : !!obj[key]));
    }

    isNotEmptyNode(obj) {
        return this.isNotEmptyObject(obj) || (!this.isObject(obj) && (Array.isArray(obj) ? obj.length : !!obj));
    }

    getKeys(obj) {
        return Object.keys(obj);
    }

}