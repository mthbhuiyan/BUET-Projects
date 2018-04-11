# include "iGraphics.h"

int sb[2][3]={{0,128,128},{0,191,255}},color[3]={1},menu=0,wmode[3]={},dmode=0,cw=0,cd=0,i=0,output=0;
// highest number of word dictionary can hold,  highest legth of a word, highest legth of details
const int limit = 10000, word_length = 30, details_length = 400;


struct trie
{
    int index;  // what is index of details of the word in the dictionary
    int next[26]; // next node in trie
};
trie database[limit];  // trie/prefix tree container
char word[limit][word_length], details[limit][details_length],wtemp[word_length]={},dtemp[details_length]={},point='_',line[14][word_length];
int num_of_element, cur; // number of current words in the dictionary, number of node used for trie


// this function initialize a node in trie ( think of it as a black box, no need to explicitly use this functoin )
void init( int cn )
{
    database[cn].index = -1;
    for( int i = 0; i < 26; i++ )
        database[cn].next[i] = -1;
}

//convert a string to only lower case letters
void to_lower( char str[] )
{
    int i = 0;
    while( str[i] )
    {
        if( str[i] >= 'A' && str[i] <= 'Z' )
            str[i] += 32;
        i++;
    }
}

// this finction add new word in the dictionary, complexity O(str_length) ... no need to explicitly use this functoin
void add_word( char str[], int position_in_array )
{
    int i = 0, node = 0;
    to_lower( str );
    while( str[i] )
    {
        int d = str[i] - 'a';
        if( database[node].next[d] == -1 )
        {
            init(cur);
            node = database[node].next[d] = cur++;
        }
        else
            node = database[node].next[d];
        i++;
    }
    database[node].index = position_in_array;
}

void initialize_trie()
{
    cur = 0;
    init(cur);
    num_of_element = 0;
    FILE *input_word = fopen( "word.txt", "r" );
    FILE *input_details = fopen( "details.txt", "r" );
    char str1[word_length], str2[details_length];
    while( fgets( str1, word_length, input_word ) != NULL && fgets( str2, details_length, input_details ) != NULL )
    {
        str1[strlen(str1)-1] = '\0'; //because there in a new line character before '\0'
        str2[strlen(str2)-1] = '\0'; //because there in a new line character before '\0'
        strcpy( word[num_of_element], str1 );
        strcpy( details[num_of_element], str2 );
        add_word( str1, num_of_element++ );
    }
    fclose(input_word);
    fclose(input_details);
}

// this finction add new word given by the user in the dictionary

void update( char str1[], char str2[] ) //new word, its details
{
    strcpy( word[num_of_element], str1 );
    strcpy( details[num_of_element], str2 );
    add_word( str1, num_of_element++ );
}

// this finction return the index of word_details in the details array, complexity O(str_length)
int query( char str[] ) //word search
{
    int i = 0, node = 0;
    to_lower( str );
    while( str[i] )
    {
        int d = str[i] - 'a';
        if( database[node].next[d] == -1 )
            return -1;
        else
            node = database[node].next[d];
        i++;
    }
    return database[node].index;
}

//closes the dictionary and save all the word in a file
void close_dictionary()
{
    FILE *output_word = fopen( "word.txt", "w" );
    FILE *output_details = fopen( "details.txt", "w" );
    for( int i = 0; i < num_of_element; i++ )
    {
        fputs( word[i], output_word );
        fputs( details[i], output_details );
    }
}

void Search_menu()
{
    int n;
    iSetColor(0,0,0);
    iRectangle(20,310,300,25);
    iText(20,340,"Word",GLUT_BITMAP_HELVETICA_18);
    iText(25,320,wtemp);
    if(output)
    {
        n=query(wtemp);
        if(n!=-1)
            iText(20,200,details[n]);
        else
            iText(20,200,"Word Not Found");
    }
}
void Add_menu()
{
    iSetColor(0,0,0);
    iRectangle(20,310,300,25);
    iRectangle(20,250,800,25);
    iText(20,340,"Word",GLUT_BITMAP_HELVETICA_18);
    iText(25,320,wtemp);
    iText(20,280,"Details",GLUT_BITMAP_HELVETICA_18);
    iText(25,260,dtemp);
    if(output)
    {
        update(wtemp,dtemp);
        iText(20,200,"Successfully Added");
    }
}
void Modify_menu()
{
    iSetColor(0,0,0);
    iRectangle(20,310,300,25);
    iText(20,340,"Word",GLUT_BITMAP_HELVETICA_18);
    iText(25,320,wtemp);
    if(output)
    {

    }
}


int mask(int mx, int my, int lx, int ly, int ux, int uy)
{
    if((mx>=lx)&&(mx<=ux)&&(my>=ly)&&(my<=uy))
        return 1;
    else
        return 0;
}

void iDraw()
{
    iClear();
    iSetColor(255,255,255);
    iFilledRectangle(0,0,900,400);
    iSetColor(sb[color[0]][0],sb[color[0]][1],sb[color[0]][2]);
    iFilledRectangle(2,368,297,30);
    iSetColor(sb[color[1]][0],sb[color[1]][1],sb[color[1]][2]);
    iFilledRectangle(301,368,298,30);
    iSetColor(sb[color[2]][0],sb[color[2]][1],sb[color[2]][2]);
    iFilledRectangle(601,368,297,30);
    iSetColor(255,255,255);
    iText(97,376,"Search Word",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(402,376,"Add Word",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(692,376,"Modify Word",GLUT_BITMAP_TIMES_ROMAN_24);
    switch(menu)
    {
    case 1:
        Add_menu();
        break;
    case 2:
        Modify_menu();
        break;
    default:
        Search_menu();
        break;
    }

}

void iMouseMove(int mx, int my)
{
    //place your codes here
    if(my>=368&&my<=398)
    {
        if(mx>=0&&mx<300)
        {
            menu=0;
            output=0;
            color[0]=1;
            wtemp[0]='\0';
            wtemp[1]='\0';
            wmode[0]=0;
            cw=0;
        }
        else
            color[0]=0;
        if(mx>=300&&mx<600)
        {
            menu=1;
            output=0;
            color[1]=1;
            wtemp[0]='\0';
            wtemp[1]='\0';
            dtemp[0]='\0';
            dtemp[1]='\0';
            wmode[1]=0;
            dmode=0;
            cw=0;
            cd=0;
        }
        else
            color[1]=0;
        if(mx>=600&&mx<=900)
        {
            menu=2;
            output=0;
            color[2]=1;
            wtemp[0]='\0';
            wtemp[1]='\0';
            wmode[2]=0;
            cw=0;
        }
        else
            color[2]=0;
    }
}

void iMouse(int button, int state, int mx, int my)
{
    if(button == GLUT_LEFT_BUTTON && state == GLUT_DOWN)
    {
        //place your codes here
        if(my>=368&&my<=398)
        {
            if(mx>=0&&mx<300)
            {
                menu=0;
                output=0;
                color[0]=1;
                wtemp[0]='\0';
                wtemp[1]='\0';
                wmode[0]=0;
                cw=0;
            }
            else
                color[0]=0;
            if(mx>=300&&mx<600)
            {
                menu=1;
                output=0;
                color[1]=1;
                wtemp[0]='\0';
                wtemp[1]='\0';
                dtemp[0]='\0';
                dtemp[1]='\0';
                wmode[1]=0;
                dmode=0;
                cw=0;
                cd=0;
            }
            else
                color[1]=0;
            if(mx>=600&&mx<=900)
            {
                menu=2;
                output=0;
                color[2]=1;
                wtemp[0]='\0';
                wtemp[1]='\0';
                wmode[2]=0;
                cw=0;
            }
            else
                color[2]=0;
        }
        if(menu==1)
        {
            dmode=mask(mx,my,20,200,320,275);
        }
        wmode[menu]=mask(mx,my,20,310,320,335);
    }
    if(button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN)
    {
        //place your codes here
    }
}

void iKeyboard(unsigned char key)
{
    //place your codes here
    if(menu==1&&dmode==1)
    {
        if(key==8)
        {
            cd--;
            dtemp[cd]='\0';
            dtemp[cd+1]='\0';
        }
        else if(key=='\r')
        {
            dmode=0;
            output=1;
        }
        else if(cd<=details_length)
        {
            dtemp[cd]=key;
                cd++;
            dtemp[cd+1]='\0';
        }
    }
    else if(!wmode[menu])
        wmode[menu]=1;
    if(wmode[menu])
    {
        if(key==8)
        {
            cw--;
            wtemp[cw]='\0';
            wtemp[cw+1]='\0';
        }
        else if(key=='\r')
        {
            wmode[menu]=0;
            if(menu==1)
                dmode=1;
            else
                output=1;
        }
        else if(cw<=word_length)
        {
            wtemp[cw]=key;
            cw++;
            wtemp[cw+1]='\0';
        }
    }
}

void iSpecialKeyboard(unsigned char key)
{

    if(key == GLUT_KEY_END)
    {
        close_dictionary();
        exit(0);
    }

}
void pointer()
{
    if(wmode[menu])
        wtemp[cw]^='_';
    else
        wtemp[cw]='\0';
    if(menu==1&&dmode)
        dtemp[cd]^='_';
    else
        dtemp[cd]='\0';
}

int main()
{
    initialize_trie();
    iSetTimer(500,pointer);
    iInitialize(900, 400, "DIGITAL DICTIONARY");
    close_dictionary();

    return 0;
}
