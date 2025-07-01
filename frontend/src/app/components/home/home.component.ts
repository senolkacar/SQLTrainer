import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { Router, ActivatedRoute,RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [RouterLink],
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