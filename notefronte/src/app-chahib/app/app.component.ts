import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ControlComponent } from "./component/control/control.component";
import { ValiderPopupComponent } from "./component/valider-popup/valider-popup.component";
import { FormulaireComponent } from "./component/formulaire/formulaire.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ControlComponent, ValiderPopupComponent, FormulaireComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'GestDocument';
}
