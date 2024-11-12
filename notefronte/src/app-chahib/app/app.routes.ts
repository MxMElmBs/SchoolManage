import { Routes } from '@angular/router';
import { ControlComponent } from './component/control/control.component';
import { FormulaireComponent } from './component/formulaire/formulaire.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'control', component: ControlComponent },
    { path: 'formulaire', component: FormulaireComponent}

];
