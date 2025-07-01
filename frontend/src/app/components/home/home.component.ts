import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  imports: [CommonModule, RouterLink],
})
export class HomeComponent {
  returnUrl!: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
) {
    // redirect to home if already logged in
    if (this.authenticationService.currentUser) {
        this.router.navigate(['/quizzes']);
    }

}
}