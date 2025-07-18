import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild, forwardRef } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR,FormsModule } from "@angular/forms";
import { DatabaseService } from "../../services/database.service";
import { CommonModule } from "@angular/common";

import * as ace from "ace-builds";
import "ace-builds/src-noconflict/mode-mysql";
import "ace-builds/src-noconflict/ext-language_tools";
import { Observable } from "rxjs";

@Component({
    selector: "code-editor",
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
    <form #codeForm="ngForm">
        <div class="app-ace-editor" #editor style="width: 100%;margin-top:8px;margin-bottom:8px"></div>
    </form>
    `,
    styles: [`
      .app-ace-editor {
        border: 2px solid #f8f9fa;
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
      }`,
    ],
    // Déclare un fournisseur de NG_VALUE_ACCESSOR pour indiquer comment le composant doit fonctionner en tant que 
    // contrôle Angular, ce qui permet de lier des valeurs bidirectionnelles avec [(ngModel)] ou [ngModel].
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            // indique à Angular quel composant doit être utilisé pour le binding
            useExisting: forwardRef(() => CodeEditorComponent),
            // doit tjrs être true
            multi: true,
        }
    ]
})
export class CodeEditorComponent implements AfterViewInit, ControlValueAccessor {

    /*
    L'interface ControlValueAccessor est une partie essentielle de la configuration qui permet à un composant 
    personnalisé d'être utilisé avec ngModel pour la liaison de données bidirectionnelle. Elle détermine comment 
    le composant interagit avec ngModel et quelles méthodes et propriétés il doit mettre en œuvre. Cette interface 
    permet à un composant d'agir comme un contrôle Angular.
    */

    constructor(
        private databaseService : DatabaseService
    ) { }

    // permet d'accéder à l'objet DOM qui correspond au DIV qui contient l'éditeur
    @ViewChild("editor") private _editor!: ElementRef<HTMLElement>;
    // contient le code entré dans l'éditeur
    private _code: string = "";
    // référence vers une interface de la lib pour pouvoir configurer l'éditeur
    private _aceEditor?: ace.Ace.Editor;
    // détermine si l'éditeur est en lecture seule
    private _readOnly: boolean = false;
    // contient la fonction de callback qui sera appelée quand la valeur de l'éditeur change
    private _onChange: any;
    
    private _dataFetched: boolean = false;
    private _columns: string[] = [];
    private _tables: string[] = [];
    private _completions: any[] = [];
    @Input() dbName : string = "";

    ngOnChanges(changes: SimpleChanges): void {
        if (changes["dbName"] && !changes["dbName"].firstChange) {
            // Reset completions array
            this._completions = [];

            // Clear completions from Ace Editor's session
            if (this._aceEditor) {
                const session: any = this._aceEditor.getSession();
                const langTools = ace.require("ace/ext/language_tools");

                // Clear completions
                session.$completers = [];
                langTools.setCompleters([]);

                // Force refresh of Ace Editor
                session.setMode("ace/mode/mysql");
            }

            this.fetchData();
        }
    }
    

    ngOnInit(): void {
        //console.log("this is db name:"+this.dbName);
    }
    ngAfterViewInit(): void {
        this._aceEditor = ace.edit(this._editor.nativeElement);
        this._aceEditor!.setReadOnly(this.readOnly);
        this._aceEditor.setShowPrintMargin(false);
        // indique que l'éditeur doit permettre de gérer du code SQL pour MySQL
        this._aceEditor.session.setMode("ace/mode/mysql");
        this._aceEditor.setOptions({
            enableBasicAutocompletion: true,
            enableSnippets: false,
            enableLiveAutocompletion: true,
            minLines: 5,
            maxLines: 15,
            showGutter: false,
            fontSize: "1.5rem",
        });
        // Quand on détecte que la valeur de l'éditeur a changé, il faut déclencher le binding
        this._aceEditor.on("input", () => {
            this._code = this._aceEditor!.getValue();
            this._onChange(this._code);
        });
        this._aceEditor.setValue(this._code, -1);
        this._aceEditor.resize();
        // Configuration de la complétion automatique
        if (this._aceEditor) {
            const session: any = this._aceEditor.getSession();
            const langTools = ace.require("ace/ext/language_tools");

            // Clear completions
            session.$completers = [];
            langTools.setCompleters([]);

            // Force refresh of Ace Editor
            session.setMode("ace/mode/mysql");
        }
        this.fetchData();
        
    }

    fetchData(): void {
        // Check if dbName is available
        if (this.dbName) {
            // Fetch table and column names
            this.getTableNames(this.dbName).subscribe((tables) => {
                this._tables = tables;
                this.updateCompletions();
            });

            this.getColumnNames(this.dbName).subscribe((columns) => {
                this._columns = columns;
                this.updateCompletions();
            });
        }
    }

    updateCompletions(): void {
        // Populate completions array
        this._completions = [];

        this._tables.forEach((table: any) => {
            this._completions.push({ caption: table, value: table, meta: "Table", score: 100 });
        });

        this._columns.forEach((column: any) => {
            this._completions.push({ caption: column, value: column, meta: "Column", score: 100 });
        });

        const keywords = CodeEditorComponent.getKeywords();
        keywords.forEach((keyword: any) => {
            this._completions.push({ caption: keyword, value: keyword, meta: "Keyword", score: 75 });
        });

        // Update completions in Ace Editor
        if (this._aceEditor) {
            const langTools = ace.require("ace/ext/language_tools");
            langTools.addCompleter({
                getCompletions: (editor: any, session: any, pos: any, prefix: any, callback: any) => {
                    callback(null, this._completions);
                }
            });
        }
    }

    /**
     * Renvoie un tableau contenant les noms des tables SQL a afficher dans la complétion.
     * 
     * **TODO: Ici, géré de manière statique, mais devrait être dynamique en fonction de la BD ciblée.**
     */
    private getTableNames(dbName: string): Observable<string[]> {
        return this.databaseService.getTables(dbName);
      
        /*switch(dbName){
            case "fournisseurs":
                return ["SPJ", "S", "P", "J"];
            case "facebook":
                return ["Destinataires","EstAmi","Message","Personne"];
        }
        return [];*/

    }

    /**
     * Renvoie un tableau contenant les noms des colonnes des tables SQL a afficher dans la complétion.
     * 
     * **TODO: Ici, géré de manière statique, mais devrait être dynamique en fonction de la BD ciblée.**
     */
    private getColumnNames(dbName: string): Observable<string[]> {
        return this.databaseService.getColumns(dbName);
        /*switch(dbName){
            case "fournisseurs":
                return ["ID_S", "ID_P", "ID_J", "PNAME", "COLOR", "CITY", "JNAME", "SNAME", "STATUS", "WEIGHT", "QTY", "DATE_DERNIERE_LIVRAISON"];
            case "facebook":
                return ["ID_Message","Destinataire","SSN1","SSN2","Contenu","Date_Expedition","Expediteur","SSN","Nom","Sexe","Age"];
        }
        return [];*/
    }

    /**
     * Renvoie un tableau contenant les mots-clés du langage SQL a afficher dans la complétion.
     * Nous y avons mis les principaux, mais cette liste n'est pas exhaustive.
     */
    private static getKeywords() {
        return [
            'SELECT', 'FROM', 'WHERE', 'GROUP BY', 'HAVING', 'ORDER BY',
            'LIMIT', 'OFFSET', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN',
            'FULL OUTER JOIN', 'CROSS JOIN', 'UNION', 'UNION ALL', 'AS',
            'DISTINCT', 'ALL', 'BETWEEN', 'CASE', 'WHEN', 'THEN', 'ELSE',
            'END', 'EXISTS', 'IN', 'LIKE', 'ILIKE', 'SIMILAR TO', 'NOT', 'AND', 'OR',
            'IS NULL', 'IS NOT NULL', 'ANY', 'ALL', 'SOME', 'EXTRACT',
            'DATE_PART', 'COUNT', 'SUM', 'AVG', 'MIN', 'MAX', 'ROUND',
            'TRUNCATE', 'CONCAT', '||', 'SUBSTR', 'LEFT', 'RIGHT',
            'LOWER', 'UPPER', 'INITCAP', 'LENGTH', 'CHAR_LENGTH',
            'POSITION', 'STRPOS', 'CAST', 'COALESCE',
            'NULLIF', 'LEAD', 'LAG', 'FIRST_VALUE', 'LAST_VALUE',
            'NTH_VALUE', 'OVER', 'PARTITION BY', 'RANK', 'DENSE_RANK',
            'ROW_NUMBER', 'FETCH FIRST', 'ROWS ONLY', 'LATERAL', 'TABLE',
            'XMLTABLE', 'JSON_TABLE', 'PIVOT', 'UNPIVOT', 'ROLLUP',
            'CUBE', 'GROUPING SETS', 'FILTER', 'WINDOW', 'OVERLAPS',
            'CONTAINS', 'FREETEXT'
        ];
    }

    /*
     * Implémentation de l'interface ControlValueAccessor
     */

    // Cette méthode est appelée par Angular lorsque la valeur de la liaison de données doit être 
    // mise à jour à partir du modèle.
    writeValue(value: any) {
        this._code = value;
        if (this._aceEditor) {
            const cursorPos = this._aceEditor.getCursorPosition();
            this._aceEditor.setValue(this._code, -1);
            this._aceEditor.moveCursorToPosition(cursorPos);
        }
    }

    // Cette méthode permet d'enregistrer un callback que le composant doit appeler chaque fois 
    // que sa valeur change. Cette fonction de rappel est fournie par ngModel.
    registerOnChange(fn: any) {
        this._onChange = fn;
    }

    registerOnTouched(fn: any) {
        // pas implémentée
    }

    /**
     * Permet de donner le focus à l'éditeur de code.
     */
    focus(): void {
        this._aceEditor?.focus();
    }

    /**
     * Permet de mettre l'éditeur en mode lecture seule.
     */
    get readOnly(): boolean {
        return this._readOnly;
    }

    @Input() set readOnly(val: boolean) {
        this._readOnly = val;
        this._aceEditor?.setReadOnly(val);
    }
}