import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { GuestGuard } from './guest.guard';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { UserEditComponent } from './components/user-edit/user-edit.component';
import { UserViewComponent } from './components/user-view/user-view.component';
import { UserCreateComponent } from './components/user-create/user-create.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { ProfileComponent } from './components/profile/profile.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LandingComponent } from './components/landing/landing.component';
import { ChatComponent } from './components/chat/chat.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent, canActivate: [GuestGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [GuestGuard] },
  { path: 'forget-password', component: ForgetPasswordComponent, canActivate: [GuestGuard] },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'chat', 
    component: ChatComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'users', 
    component: UserListComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'user-list', 
    component: UserListComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'users/create', 
    component: UserCreateComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'user-create', 
    component: UserCreateComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'users/:id', 
    component: UserViewComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'users/:id/edit', 
    component: UserEditComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'user-edit/:id', 
    component: UserEditComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'user-view/:id', 
    component: UserViewComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'profile', 
    component: ProfileComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'change-password', 
    component: ChangePasswordComponent,
    canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { } 