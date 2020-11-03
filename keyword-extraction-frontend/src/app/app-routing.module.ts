import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TrainComponent } from './train/train.component';
import { TestComponent } from './test/test.component';

const appRoutes: Routes = [
  { path: '', redirectTo: '/test', pathMatch: 'full' },
  { path: 'train', component: TrainComponent },
  { path: 'test', component: TestComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
