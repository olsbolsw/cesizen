import { Component, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AdminService } from '../../../core/services/admin.service';
import { User } from '../../../core/models/user.model';
import { Role } from '../../../core/models/role.enum';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [DatePipe, MatCardModule, TableModule, TagModule],
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.scss',
})
export class AdminUsersComponent implements OnInit {
  private readonly adminService = inject(AdminService);

  users: User[] = [];
  readonly Role = Role;

  ngOnInit(): void {
    this.adminService.listUsers().subscribe({
      next: (data) => (this.users = data),
    });
  }

  roleLabel(role: Role): string {
    return role === Role.ADMIN ? 'Administrateur' : 'Utilisateur';
  }
}
