import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TrainComponent } from './train/train.component';
import { TestComponent } from './test/test.component';
import { SearchComponent } from './search/search.component';

const appRoutes: Routes = [
  { path: '', redirectTo: '/search', pathMatch: 'full' },
  { path: 'search', component: SearchComponent },
  { path: 'test', component: TestComponent },
  { path: 'train', component: TrainComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
