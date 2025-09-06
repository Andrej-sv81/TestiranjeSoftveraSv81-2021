import { Component } from '@angular/core';
import { RegistrationService } from '../registration.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-picture-pup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './picture-pup.component.html',
  styleUrl: './picture-pup.component.css'
})
export class PicturePUPComponent {
  imageUrls: string[] = [];
  selectedFiles: File[] = [];
  email: string | null = null;
  error: boolean = false;

  constructor(private service: RegistrationService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.email = this.route.snapshot.queryParamMap.get('email');
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const newFiles = Array.from(input.files);

      let files = [...this.selectedFiles, ...newFiles];

      if (files.length > 5) {
        files = files.slice(files.length - 5);
      }

      this.selectedFiles = files;
      this.imageUrls = [];
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.imageUrls.push(e.target.result);
        };
        reader.readAsDataURL(file);
      });
    }
  }

  uploadPictures() {
    if (this.selectedFiles.length === 0 || !this.email) return;
    const formData = new FormData();
    this.selectedFiles.forEach(file => formData.append('images', file));
    console.log(formData.getAll('images'));
    this.service.sendPicturePup(formData, this.email).subscribe({
      next: () => {
        this.error = false;
        this.router.navigate(['/login']);
      },
      error: () => {
        this.error = true;
      }
    });
  }
}
