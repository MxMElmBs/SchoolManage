import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../header/header.component';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterModule, HeaderComponent],
  templateUrl: './dashboardjeff.component.html',
  styleUrl: './dashboardjeff.component.css'
})
export class DashboardjeffComponent implements OnInit {
  isDisabled: boolean | undefined;

  constructor() {}

  ngOnInit(): void {

  }

}
