import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { QuizListComponent } from '../quizzes/quiz-list.component';
import { Router } from '@angular/router';
import { FilterComponent } from '../quizzes/filter.component';

@Component({
    selector: 'app-teacher',
    imports: [QuizListComponent, FilterComponent],
    templateUrl: './teacher.component.html',
})
export class TeacherComponent{
    @ViewChild(QuizListComponent) child!: QuizListComponent
    filter: string = '';
    constructor(
        private router: Router
    ) {}

    onFilterChanged(value: string) {
        this.filter = value;
    }

    
    createQuiz() {
       this.router.navigate(['/quizedition/0']);
    }
}