import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { StudentsIDashBComponent } from "./component/StudentsIDashB/students-idash-b/students-idash-b.component";
import { StudentsIHeaderComponent } from "./component/StudentsIHeader/students-iheader/students-iheader.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'StudentsInterfaces';
}
