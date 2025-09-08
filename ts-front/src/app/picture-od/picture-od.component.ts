import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegistrationService } from '../registration.service';
import { AuthService } from '../auth/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-picture-od',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './picture-od.component.html',
  styleUrl: './picture-od.component.css'
})
export class PictureODComponent {
  imageUrl: string = 'assets/default.jpeg';
  selectedFile: File | null = null;
  email: string | null = null;
  error: boolean = false;

  constructor(private service: RegistrationService, private auth: AuthService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.email = this.route.snapshot.queryParamMap.get('email')
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageUrl = e.target.result;
      };
      reader.readAsDataURL(this.selectedFile);

      const formData = new FormData();
      formData.append('image', this.selectedFile);
      
      this.service.sendPictureOd(formData, this.email!).subscribe({
        next: (response: any) => {
          this.error = false;
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000); // 1 second delay
        },
        error: (err: any) => {
          this.error = true;
          console.error('Error uploading picture:', err);
        }
      });
    }
  } 
}
