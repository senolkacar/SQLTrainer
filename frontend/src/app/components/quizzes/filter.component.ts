import { Component,Output,EventEmitter } from '@angular/core';
import { MatFormField } from '@angular/material/form-field';
import { MatLabel } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-filter',
    imports: [MatFormField,MatLabel, FormsModule],
    templateUrl: './filter.component.html',
  })
export class FilterComponent{
    @Output() filterChangedEvent = new EventEmitter<string>();
    filter: string = '';
    
    filterChanged(value: string) {
       this.filterChangedEvent.emit(this.filter);
    }
}