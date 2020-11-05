import { Keyword } from "./keyword.model";

export class Advertisement {
    constructor(public title: string, public content: string, public keywords: Keyword[]) {}
}
