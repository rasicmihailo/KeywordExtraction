import { Component, OnInit } from '@angular/core';
import { Advertisement } from '../shared/Advertisement.model';
import { Keyword } from '../shared/keyword.model';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchText: string = '';

  private advertisement: Advertisement;

  private advertisements: Advertisement[] = [];

  private keywords: Keyword[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get(`http://localhost:8080/keywords`).subscribe(
      data => {
        this.keywords = JSON.parse(JSON.stringify(data));
      },
      error => {
      });
  }

  search() {
    this.advertisement = null;
    this.http.get(`http://localhost:8080/search?s=` + this.searchText).subscribe(
      data => {
        this.advertisements = JSON.parse(JSON.stringify(data));
      },
      error => {
      });
  }
  searchByKeyword(keyword: Keyword) {
    this.searchText = keyword.stem;
    this.search();
  }

  advertisementCLicked(advertisement: Advertisement) {
    this.advertisement = advertisement;
  }
}
