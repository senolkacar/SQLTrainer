import { Component, OnInit, ViewChild, AfterViewInit, ElementRef, OnDestroy, Input } from '@angular/core';
import * as _ from 'lodash-es';
import { Quiz } from '../../models/quiz';
import { QuizService } from '../../services/quiz.service';
import { StateService } from '../../services/state.service';
import { QuizStateService } from '../../services/quiz-state.service';
import { MatTableState } from '../../helpers/mattable.state';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from "@angular/material/sort";
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from '../../services/authentication.service';
import { format, formatISO } from 'date-fns';
import { plainToClass } from 'class-transformer';
import { MatTable } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatColumnDef } from '@angular/material/table';
import { MatHeaderCellDef } from '@angular/material/table';
import { MatCellDef } from '@angular/material/table';
import { MatHeaderRow } from '@angular/material/table';
import { MatRowDef } from '@angular/material/table';

@Component({
  selector: 'app-quiz-list',
  templateUrl: './quiz-list.component.html',
  imports: [MatTable, MatPaginator, MatSort,MatIcon, CommonModule,MatColumnDef, MatHeaderCellDef,MatCellDef,MatHeaderRow, MatRowDef],
  styleUrls: ['./quiz-list.component.css']
})
export class QuizListComponent {
    private _filter: string = '';
    get filter(): string {
        return this._filter;
    }
    @Input() set filter(value: string) {
        this._filter = value;
        this.filterChanged(value);
    }

    @Input() quizType: 'test' | 'training' | 'teacher' = 'test';
    displayedColumns: string[] = [];


  dataSource: MatTableDataSource<Quiz> = new MatTableDataSource();
  state: MatTableState;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private quizService: QuizService,
    private stateService: StateService,
    private quizStateService: QuizStateService,
    private authService: AuthenticationService,
    private router: Router,
    public dialog: MatDialog,
    public snackBar: MatSnackBar
  ) {
    this.state = this.stateService.quizListState;
  }

  ngOnInit():void{
    if(this.quizType === 'teacher'){
      this.displayedColumns = ['name', 'databaseName','type', 'statutForTeacher', 'testStart', 'testEnd', 'actions'];
    }else if(this.quizType === 'training'){
      this.displayedColumns = ['name', 'databaseName', 'statut', 'actions'];
    }else{
      this.displayedColumns = ['name', 'databaseName', 'testStart', 'testEnd', 'statut', 'evaluation', 'actions'];
    }
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.dataSource.filterPredicate = (data: Quiz, filter: string) => {
      const str = data.name + ' ' + data.statut + ' ' + data.database?.name + ' ' + data.type + ' ' + data.statutForTeacher;
      return str.toLowerCase().includes(filter);
    };

    this.state.bind(this.dataSource);
    this.refresh();
  }

  refresh() {
    let quizObservable;
    if(this.quizType === 'teacher'){
      quizObservable = this.quizService.getAll();
    }else if(this.quizType === 'training'){
      quizObservable = this.quizService.getTrainings();
    }else{
      quizObservable = this.quizService.getTests();
    }
    
    quizObservable.subscribe(data => {
        this.dataSource.data = data;
        this.state.restoreState(this.dataSource);
        this.filter = this.state.filter;
    });
  }


  filterChanged(filterValue: string) {
    if (filterValue == null) {
      filterValue = '';
    }

    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.state.filter = this.dataSource.filter;

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  continueQuiz(quizId: number): void {
    this.quizStateService.setReadOnlyMode(false);
    // Navigate to the first question of the selected quiz
    this.quizService.getFirstQuestionId(quizId).subscribe(questionId => {
      this.router.navigate(['/question', questionId]);
    });
  }

  editQuiz(quizId: number): void {
    this.router.navigate(['/quizedition', quizId]);
  }


  readQuiz(quizId: number):void {
    this.quizStateService.setReadOnlyMode(true);
    // Navigate to the first question of the selected quiz on only read mode
    this.quizService.getFirstQuestionId(quizId).subscribe(questionId => {
      this.router.navigate(['/question', questionId]);
    });
  }

  startQuiz(quizId: number) {
    this.quizService.createAttempt(quizId).subscribe(response => {
      this.quizService.getFirstQuestionId(quizId).subscribe(questionId => {
        this.router.navigate(['/question', questionId]);
      });
    });
    
  }

  ngOnDestroy(): void {
    this.snackBar.dismiss();
  }
}