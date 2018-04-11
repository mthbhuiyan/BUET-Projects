# include "iGraphics.h"

struct obj
{
    float tx,ty;
    int dx,dy,length;
    char data[100];
    int mode;
} ;
struct obj temp;
struct obj bubble[100],bubble_saved[100];

FILE * fp;

char crsr='_',description[100];

int bubble_i=0,bubble_j=0,max=0;
int index[4],excng[2],x,d,middle,switch_mode[6]= {6,4,4,4,0,4};
int working=0,pause=0,option_mode[2]= {6,6};
int order=0,lex=0,k,manual_reset=0,sort_what=1,sort_type=0,auto_generate=1;
int negmark=0,decpoint=0,selectn[100],speed_button=0,speed_mode=0,speed=-1;
int color[11][3]= {{0,191,255},{255,215,0},{60,179,113},{255,20,60},{70,130,180},{60,111,154},{128,128,128},{200,200,200},{255,165,0},{204,0,102},{178,102,255}};
int set_button_mode=0,final_button_mode=0,pause_button_mode=0,prev_button_mode=0,nxt_button_mode=0;

double size_char[2][26]= {{11,12,11,12,11,7,12,13,6,6,12,6,20,13,12,12,12,8,10,7,13,11,17,13,11,10},
    {17,16,16,17,15,14,18,19,8,11,17,14,22,18,18,15,18,16,13,16,18,17,23,18,16,15}
},
size_punc=6,size_no=12,l=0,vx,vy,speed_pos=30,change_color,x_pause[3]= {646,646,656},y_pause[3]= {59,71,65},
        x_previous[6]= {613,623,623,616,623,623},x_next[6]= {687,677,677,684,677,677},y_cmn[6]= {65,72,69,65,61,58},
                       x_set[3]= {596,596,586},x_final[3]= {704,704,714},
                                 x_edit[5]= {555,568,570,557,553},y_edit[5]= {61,74,71,58,56},
x_speed[2][3]= {{729,729,739},{739,739,749}},x_desc_i[6]= {146,136,136,143,136,136},x_desc_f[6]= {133,143,143,136,143,143};

void bubble_sort(void);
void selection_sort(void);
void insertion_sort(void);
void merge_sort(void);
void quick_sort(void);
void write(int,int);
void read(void);
void swap_object(void);

void correct_bubble()
{
    int t;
    char temporary[100];
    for(t=0; t<=bubble_i; t++,bubble[t].tx=bubble[t-1].tx+bubble[t-1].dx+10)
    {
        itoa(atoi(bubble[t].data),bubble[t].data,10);
        bubble[t].length=strlen(bubble[t].data);
        bubble[t].dx=bubble[t].length*size_no+2;
    }
}

void scale_bubble()
{
    int t;
    max=0;
    for(t=0; t<=bubble_i; t++)
    {
        if(max<bubble[t].dx)
            max=bubble[t].dx;
    }
    for(t=1; t<=bubble_i; t++)
    {
        bubble[t].tx=bubble[t-1].tx+max+11;
    }
}

void column_auto()
{
    int t;
    for(t=0; t<=bubble_i; t++)
    {
        bubble[t].dy=atoi(bubble[t].data)+1;
    }
}

void max_length()
{
    int t;
    l=0;
    for(t=0; t<=bubble_i; t++)
    {
        negmark=(bubble[t].data[0]=='-');
        if(l<bubble[t].length-negmark)
            l=bubble[t].length-negmark;
    }
    negmark=0;
}

void random_number_generator()
{
    int n;
    srand(time(NULL));
    n=rand()%10+10;
    bubble[0].tx=10;
    for(bubble_i=0; bubble_i<n; bubble_i++,bubble[bubble_i].tx=bubble[bubble_i-1].tx+bubble[bubble_i-1].dx+10)
    {
        itoa(rand()%100,bubble[bubble_i].data,10);
        bubble[bubble_i].length=strlen(bubble[bubble_i].data);
        bubble[bubble_i].dx=bubble[bubble_i].length*size_no+2;
        bubble[bubble_i].dy=30;
        bubble[bubble_i].ty=190;
        bubble[bubble_i].mode=0;
    }
    bubble_i--;
    scale_bubble();
    column_auto();
    max_length();
    middle=650-(bubble[bubble_i].tx+max+10)/2;
}

void random_string_generator()
{
    FILE*fps;
    fps=fopen("string list","rb");
    int n,k;
    char str[9];
    srand(time(NULL));
    n=rand()%5+5;
    bubble[0].tx=10;
    for(bubble_i=0; bubble_i<n; bubble_i++,bubble[bubble_i].tx=bubble[bubble_i-1].tx+bubble[bubble_i-1].dx+10)
    {
        fseek(fps,sizeof(str)*(rand()%797),SEEK_SET);
        fread(bubble[bubble_i].data,sizeof(str),1,fps);
        bubble[bubble_i].length=strlen(bubble[bubble_i].data);
        for(k=0,bubble[bubble_i].dx=0; k<bubble[bubble_i].length; k++)
        {
            bubble[bubble_i].dx+=size_char[0][bubble[bubble_i].data[k]-'a'];
        }
        bubble[bubble_i].dx+=2;
        bubble[bubble_i].dy=30;
        bubble[bubble_i].ty=190;
        bubble[bubble_i].mode=0;
    }
    fclose(fps);
    bubble_i--;
    scale_bubble();
    max_length();
    middle=650-(bubble[bubble_i].tx+max+10)/2;
}

void reset_bubble()
{
    bubble_i=0,bubble_j=0,working=0,negmark=0,decpoint=0;
    crsr='_';
    bubble[bubble_i].data[0]='\0';
    bubble[bubble_i].data[1]='\0';
    bubble[bubble_i].tx=10;
    bubble[bubble_i].ty=190;
    bubble[bubble_i].mode=0;
    bubble[bubble_i].dx=14;
    bubble[bubble_i].dy=30;
    bubble[bubble_i].length=0;
    fclose(fp);
    iPauseTimer(index[0]);
    iPauseTimer(index[1]);
    iPauseTimer(index[3]);
    if(auto_generate)
    {
        manual_reset=0;
        if(sort_what)random_number_generator();
        else random_string_generator();
        iPauseTimer(index[2]);
    }
    else
    {
        iResumeTimer(index[2]);
        manual_reset=1;
    }
}

void copy_all_bubbles(struct obj put[],struct obj get[])
{
    int m;
    for(m=0; m<=bubble_i; m++)
        put[m]=get[m];
}

void bubble_color()
{
    iSetColor(color[6][0],color[6][1],color[6][2]);
    iFilledRectangle(1070,490,225,29);
    iFilledRectangle(1070,460,225,29);
    iFilledRectangle(1070,430,225,29);
    iSetColor(color[1][0],color[1][1],color[1][2]);
    iFilledCircle(1090,505,10);
    iSetColor(color[3][0],color[3][1],color[3][2]);
    iFilledCircle(1090,475,10);
    iSetColor(color[2][0],color[2][1],color[2][2]);
    iFilledCircle(1090,445,10);
    iSetColor(255,255,255);
    iText(1110,495,"Under Condition",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,465,"Condition True",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,435,"Sorted",GLUT_BITMAP_TIMES_ROMAN_24);
}

void insertion_color()
{
    iSetColor(color[6][0],color[6][1],color[6][2]);
    iFilledRectangle(1070,490,225,29);
    iFilledRectangle(1070,460,225,29);
    iFilledRectangle(1070,430,225,29);
    iFilledRectangle(1070,400,225,29);
    iSetColor(color[1][0],color[1][1],color[1][2]);
    iFilledCircle(1090,505,10);
    iSetColor(color[8][0],color[8][1],color[8][2]);
    iFilledCircle(1090,475,10);
    iSetColor(color[3][0],color[3][1],color[3][2]);
    iFilledCircle(1090,445,10);
    iSetColor(color[2][0],color[2][1],color[2][2]);
    iFilledCircle(1090,415,10);
    iSetColor(255,255,255);
    iText(1110,495,"Under Condition",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,465,"Condition True",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,435,"Copied to Temp",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,405,"Partly Sorted",GLUT_BITMAP_TIMES_ROMAN_24);
}

void merge_color()
{
    iSetColor(color[6][0],color[6][1],color[6][2]);
    iFilledRectangle(1070,490,225,29);
    iFilledRectangle(1070,460,225,29);
    iFilledRectangle(1070,430,225,29);
    iFilledRectangle(1070,400,225,29);
    iSetColor(color[1][0],color[1][1],color[1][2]);
    iFilledCircle(1090,505,10);
    iSetColor(color[8][0],color[8][1],color[8][2]);
    iFilledCircle(1090,475,10);
    iSetColor(color[3][0],color[3][1],color[3][2]);
    iFilledCircle(1090,445,10);
    iSetColor(color[2][0],color[2][1],color[2][2]);
    iFilledCircle(1090,415,10);
    iSetColor(255,255,255);
    iText(1110,495,"Sorted Sub-array1",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,465,"Sorted Sub-array2",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,435,"Copy Process",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,405,"Partially Sorted",GLUT_BITMAP_TIMES_ROMAN_24);
}

void quick_color()
{
    iSetColor(color[6][0],color[6][1],color[6][2]);
    iFilledRectangle(1070,490,225,29);
    iFilledRectangle(1070,460,225,29);
    iFilledRectangle(1070,430,225,29);
    iFilledRectangle(1070,400,225,29);
    iFilledRectangle(1070,370,225,29);
    iSetColor(color[1][0],color[1][1],color[1][2]);
    iFilledCircle(1090,505,10);
    iSetColor(color[9][0],color[9][1],color[9][2]);
    iFilledCircle(1090,475,10);
    iSetColor(color[10][0],color[10][1],color[10][2]);
    iFilledCircle(1090,445,10);
    iSetColor(color[3][0],color[3][1],color[3][2]);
    iFilledCircle(1090,415,10);
    iSetColor(color[2][0],color[2][1],color[2][2]);
    iFilledCircle(1090,385,10);
    iSetColor(255,255,255);
    iText(1110,495,"Under Condition",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,465,"Pivot",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,435,"Condition True",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,405,"Condition False",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1110,375,"Sorted",GLUT_BITMAP_TIMES_ROMAN_24);
}

void sort_switch()
{
    iSetColor(color[option_mode[0]][0],color[option_mode[0]][1],color[option_mode[0]][2]);
    iFilledRectangle(0,520,100,29);
    iSetColor(color[option_mode[1]][0],color[option_mode[1]][1],color[option_mode[1]][2]);
    iFilledRectangle(0,490,100,29);

    iSetColor(255,255,255);
    iText(10,530,"Object",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(10,500,"Order",GLUT_BITMAP_TIMES_ROMAN_24);

    if(option_mode[0]==7)
    {
        iSetColor(color[sort_what+6][0],color[sort_what+6][1],color[sort_what+6][2]);
        iFilledRectangle(101,520,100,29);
        iSetColor(color[!sort_what+6][0],color[!sort_what+6][1],color[!sort_what+6][2]);
        iFilledRectangle(101,490,100,29);
        iSetColor(255,255,255);
        iText(110,530,"Number",GLUT_BITMAP_TIMES_ROMAN_24);
        iText(110,500,"String",GLUT_BITMAP_TIMES_ROMAN_24);
    }

    else if(option_mode[1]==7)
    {
        iSetColor(color[!order+6][0],color[!order+6][1],color[!order+6][2]);

        iFilledRectangle(101,520,180,29);
        iSetColor(color[order+6][0],color[order+6][1],color[order+6][2]);
        iFilledRectangle(101,490,180,29);
        iSetColor(color[lex+6][0],color[lex+6][1],color[lex+6][2]);
        iFilledRectangle(101,460,180,29);
        iSetColor(255,255,255);
        iText(110,530,"Ascending",GLUT_BITMAP_TIMES_ROMAN_24);
        iText(110,500,"Descending",GLUT_BITMAP_TIMES_ROMAN_24);
        iText(110,470,"Lexicographical",GLUT_BITMAP_TIMES_ROMAN_24);
    }

}

void input_switch()
{
    iSetColor(color[auto_generate+6][0],color[auto_generate+6][1],color[auto_generate+6][2]);
    iFilledRectangle(65,520,100,29);
    iSetColor(color[!auto_generate+6][0],color[!auto_generate+6][1],color[!auto_generate+6][2]);
    iFilledRectangle(65,490,100,29);
    iSetColor(255,255,255);
    iText(75,530,"Auto",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(75,500,"Manual",GLUT_BITMAP_TIMES_ROMAN_24);
}

void description_switch()
{
    //iSetColor(color[4][0],color[4][1],color[4][2]);
    iSetColor(204,0,204);
    iFilledRectangle(0,50,130,30);//description button
    iFilledRectangle(131,50,18,30);
    if(switch_mode[4])
    {
        iFilledRectangle(0,2,540,47);
        iSetColor(255,255,255);
        iFilledPolygon(x_desc_f,y_cmn,6);
        if(working)
            iText(10,20,description,GLUT_BITMAP_TIMES_ROMAN_24);
    }
    else
    {
        iSetColor(255,255,255);
        iFilledPolygon(x_desc_i,y_cmn,6);
    }
    iText(10,60,"Description",GLUT_BITMAP_TIMES_ROMAN_24);
}

void color_switch()
{
    iSetColor(color[6][0],color[6][1],color[6][2]);
    iFilledRectangle(1070,520,225,29);
    iSetColor(color[0][0],color[0][1],color[0][2]);
    iFilledCircle(1090,535,10);
    iSetColor(255,255,255);
    iText(1110,525,"Default",GLUT_BITMAP_TIMES_ROMAN_24);
    switch(sort_type)
    {
    case 0:
    case 1:
        bubble_color();
        break;
    case 2:
        insertion_color();
        break;
    case 3:
        merge_color();
        break;
    case 4:
        quick_color();
        break;
    }
}

void window()
{
    iSetColor(255,255,255);
    iFilledRectangle(0,0,1300,600);//window
    iSetColor(70,130,180);
    iFilledRectangle(0,550,1300,50);//Top bar
    if(speed_button)
    {
        iSetColor(color[7][0],color[7][1],color[7][2]);
        iFilledRectangle(756,50,68,30);
        iSetColor(255,255,255);
        iFilledRectangle(760,64,60,2);
        iSetColor(70,130,180);
        iFilledEllipse(820-speed_pos,65,3,7,100);
    }
    iSetColor(color[switch_mode[1]][0],color[switch_mode[1]][1],color[switch_mode[1]][2]);
    iFilledRectangle(0,550,65,30);//sort button
    iSetColor(color[switch_mode[2]][0],color[switch_mode[2]][1],color[switch_mode[2]][2]);
    iFilledRectangle(65,550,75,30);//input button
    iSetColor(color[switch_mode[3]][0],color[switch_mode[3]][1],color[switch_mode[3]][2]);
    iFilledRectangle(1165,550,85,30);//color button
    iSetColor(color[!sort_type+4][0],color[!sort_type+4][1],color[!sort_type+4][2]);
    iFilledRectangle(410,555,90,30);//bubble button
    iSetColor(color[!(sort_type-1)+4][0],color[!(sort_type-1)+4][1],color[!(sort_type-1)+4][2]);
    iFilledRectangle(500,555,110,30);//selection button
    iSetColor(color[!(sort_type-2)+4][0],color[!(sort_type-2)+4][1],color[!(sort_type-2)+4][2]);
    iFilledRectangle(610,555,110,30);//insertion button
    iSetColor(color[!(sort_type-3)+4][0],color[!(sort_type-3)+4][1],color[!(sort_type-3)+4][2]);
    iFilledRectangle(720,555,85,30);//merge button
    iSetColor(color[!(sort_type-4)+4][0],color[!(sort_type-4)+4][1],color[!(sort_type-4)+4][2]);
    iFilledRectangle(805,555,85,30);//quick button
    iSetColor(color[switch_mode[5]][0],color[switch_mode[5]][1],color[switch_mode[5]][2]);
    iFilledRectangle(1235,550,63,30);//quit button
    iSetColor(color[switch_mode[0]][0],color[switch_mode[0]][1],color[switch_mode[0]][2]);
    iFilledRectangle(549,10,100,30);//reset button
    iSetColor(color[!(!working&&(bubble_i+bubble_j))+6][0],color[!(!working&&(bubble_i+bubble_j))+6][1],color[!(!working&&(bubble_i+bubble_j))+6][2]);
    iFilledRectangle(651,10,100,30);//simulate button
    iSetColor(color[(!working||pause_button_mode)+6][0],color[(!working||pause_button_mode)+6][1],color[(!working||pause_button_mode)+6][2]);
    iFilledCircle(650,65,14);//pause/resume button
    iSetColor(color[(!working||prev_button_mode)+6][0],color[(!working||prev_button_mode)+6][1],color[(!working||prev_button_mode)+6][2]);
    iFilledCircle(620,65,14);//previous button
    iSetColor(color[(!working||nxt_button_mode)+6][0],color[(!working||nxt_button_mode)+6][1],color[(!working||nxt_button_mode)+6][2]);
    iFilledCircle(680,65,14);//next button
    iSetColor(color[(!working||set_button_mode)+6][0],color[(!working||set_button_mode)+6][1],color[(!working||set_button_mode)+6][2]);
    iFilledCircle(590,65,14);//set button
    iSetColor(color[(!working||final_button_mode)+6][0],color[(!working||final_button_mode)+6][1],color[(!working||final_button_mode)+6][2]);
    iFilledCircle(710,65,14);//final button
    iSetColor(color[(manual_reset||auto_generate)+6][0],color[(manual_reset||auto_generate)+6][1],color[(manual_reset||auto_generate)+6][2]);
    iFilledCircle(560,65,14);//edit button
    iSetColor(color[speed_button+6][0],color[speed_button+6][1],color[speed_button+6][2]);
    iFilledCircle(740,65,14);//speed button
    iSetColor(255,255,255);
    iFilledPolygon(x_previous,y_cmn,6);
    iFilledPolygon(x_next,y_cmn,6);
    iFilledPolygon(x_set,y_pause,3);
    iFilledRectangle(714,59,2,12);
    iFilledPolygon(x_final,y_pause,3);
    iFilledRectangle(584,59,2,12);
    iFilledPolygon(x_edit,y_edit,4);
    iPolygon(x_edit,y_edit,5);
    iFilledPolygon(x_speed[0],y_pause,3);
    iFilledPolygon(x_speed[1],y_pause,3);
    iFilledRectangle(749,59,2,12);
    if(pause)
        iFilledPolygon(x_pause,y_pause,3);
    else
    {
        iFilledRectangle(646,59,3,12);
        iFilledRectangle(651,59,3,12);
    }
    iText(569,18,"Reset",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(659,18,"Simulate",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(10,560,"Sort",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(75,560,"Input",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(420,563,"Bubble",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(510,563,"Selection",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(620,563,"Insertion",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(730,563,"Merge",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(815,563,"Quick",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1175,560,"Color",GLUT_BITMAP_TIMES_ROMAN_24);
    iText(1245,560,"Quit",GLUT_BITMAP_TIMES_ROMAN_24);
    if(switch_mode[1]==5)
        sort_switch();
    else if(switch_mode[2]==5)
        input_switch();
    if(switch_mode[3]==5)
        color_switch();
    description_switch();
}

void show_bubble()
{
    int n,t;
    for(n=0; n<=bubble_i; n++)
    {
        if(manual_reset)
        {
            middle=650-(bubble[bubble_i].tx+bubble[bubble_i].dx+10)/2;
            iSetColor(color[bubble[n].mode][0],color[bubble[n].mode][1],color[bubble[n].mode][2]);
            iFilledRectangle(middle+bubble[n].tx-5,bubble[n].ty-10,bubble[n].dx+9,bubble[n].dy);
            iSetColor(0,0,0);
            iText(middle+bubble[n].tx,bubble[n].ty,bubble[n].data,GLUT_BITMAP_TIMES_ROMAN_24);
        }
        else if((auto_generate||l<3)&&sort_what&&!lex)
        {
            negmark=(bubble[n].data[0]=='-');
            iSetColor(color[bubble[n].mode][0],color[bubble[n].mode][1],color[bubble[n].mode][2]);
            iFilledRectangle(middle+bubble[n].tx-5,bubble[n].ty-10,max+9,bubble[n].dy);
            iSetColor(0,0,0);
            iText(middle+bubble[n].tx+(max-bubble[n].dx)/2,bubble[n].ty-30*!negmark,bubble[n].data,GLUT_BITMAP_TIMES_ROMAN_24);
            negmark=0;
        }
        else if(!manual_reset&&sort_what&&!lex)
        {
            negmark=(bubble[n].data[0]=='-');
            /*for(t=0,change_color=-(l-1)*200/l/2; t<l-bubble[n].length; t++,change_color+=200/l)
            {
                iSetColor(color[bubble[n].mode][0]+change_color,color[bubble[n].mode][1]+change_color,color[bubble[n].mode][2]+change_color);
                iFilledRectangle(middle+bubble[n].tx-5+t*(max+9)/l,bubble[n].ty-10,(max+9)/l,0);
            }*/
            iSetColor(color[bubble[n].mode][0],color[bubble[n].mode][1],color[bubble[n].mode][2]);
            iFilledRectangle(middle+bubble[n].tx-5,bubble[n].ty-10,max+9,1);
            for(t=negmark,change_color=-(l-1)*200/l/2+200/l*(l-bubble[n].length); t<bubble[n].length; t++,change_color+=200/l)
            {
                iSetColor(color[bubble[n].mode][0]+change_color,color[bubble[n].mode][1]+change_color,color[bubble[n].mode][2]+change_color);
                iFilledRectangle(middle-(l-bubble[n].length)*(max+9)/l/2+bubble[n].tx-5+(t+l-bubble[n].length)*(max+9)/l,bubble[n].ty-10,(max+9)/l,(2+(bubble[n].data[t]-'0')*10)*(!negmark*2-1));
            }

            iSetColor(0,0,0);
            iText(middle+bubble[n].tx+(max-bubble[n].dx)/2,bubble[n].ty-30*!negmark,bubble[n].data,GLUT_BITMAP_TIMES_ROMAN_24);
            negmark=0;
        }
        else if(!manual_reset||auto_generate)
        {
            negmark=(bubble[n].data[0]=='-');
            iSetColor(color[bubble[n].mode][0],color[bubble[n].mode][1],color[bubble[n].mode][2]);
            iFilledRectangle(middle+bubble[n].tx-5,bubble[n].ty-10,max+9,1);
            for(t=negmark,change_color=-(l-1)*200/l/2; t<bubble[n].length; t++,change_color+=200/l)
            {
                iSetColor(color[bubble[n].mode][0]+change_color,color[bubble[n].mode][1]+change_color,color[bubble[n].mode][2]+change_color);
                iFilledRectangle(middle+(l-bubble[n].length-negmark)*(max+9)/l/2+bubble[n].tx-5+t*(max+9)/l,bubble[n].ty-10,(max+9)/l,2+(bubble[n].data[t]-'.')*2*!sort_what+(bubble[n].data[t]-'0')*10*sort_what);
            }
            /*for(; t<l; t++,change_color+=200/l)
            {
                iSetColor(color[bubble[n].mode][0]+change_color,color[bubble[n].mode][1]+change_color,color[bubble[n].mode][2]+change_color);
                iFilledRectangle(middle+bubble[n].tx-5+t*(max+9)/l,bubble[n].ty-10,(max+9)/l,0);
            }*/

            iSetColor(0,0,0);
            iText(middle+bubble[n].tx+(max-bubble[n].dx)/2,bubble[n].ty-30,bubble[n].data,GLUT_BITMAP_TIMES_ROMAN_24);
        }
    }
}

void iDraw()
{
    //place your drawing codes here
    iClear();
    window();
    show_bubble();
}

int mask(int mx, int my, int lx, int ly, int ux, int uy)
{
    if((mx>=lx)&&(mx<=ux)&&(my>=ly)&&(my<=uy))
        return 1;
    else
        return 0;
}

void pause_button_func()
{
    if(working)
    {
        pause^=1;
        if(pause)
            iPauseTimer(index[k]);
        else
        {
            speed=-1;
            if(sort_type==3&&excng[0]!=-1)
            {
                if(k==3);
                else if(excng[1]==-1)
                {
                    vx=0;
                    vy=-6;
                    x=190;
                }
                else
                {
                    vx=6*(excng[1]-bubble[excng[0]].tx)/180;
                    vy=6;
                    x=370;
                }
                k=3;
                iResumeTimer(index[3]);
            }
            else if(excng[1]!=-1)
            {
                if(k==1)
                    iResumeTimer(index[1]);
                else
                    swap_object();
            }

            else if(excng[0]!=-1)
            {
                if(k!=3)
                {
                    x=560-bubble[excng[0]].ty;
                    vy=6*(x-bubble[excng[0]].ty)/180;
                    k=3;
                }
                iResumeTimer(index[3]);
            }
            else
            {
                k=0;
                iResumeTimer(index[0]);
            }
        }
    }
}

void prev_button_func()
{
    iPauseTimer(index[k]);
    fseek(fp,-2*sizeof(struct obj)*(bubble_i+1)-4*sizeof(int)-2*sizeof(description),SEEK_CUR);
    read();
    k=0;
    if(!pause)iResumeTimer(index[0]);
}

void nxt_button_func()
{
    iPauseTimer(index[k]);
    read();
    k=0;
    if(!pause)iResumeTimer(index[0]);
}

void final_button_func()
{
    iPauseTimer(index[k]);
    pause=1;
    k=0;
    fseek(fp,-sizeof(struct obj)*(bubble_i+1)-2*sizeof(int)-sizeof(description),SEEK_END);
    read();
}

void set_button_func()
{
    pause=1;
    iPauseTimer(index[k]);
    fseek(fp,0,SEEK_SET);
    read();
}

void edit_button_func()
{
    manual_reset=1;
    working=0;
    pause=1;
    copy_all_bubbles(bubble,bubble_saved);
    iPauseTimer(index[k]);
    if(!auto_generate)
    {
        bubble[bubble_i].dx+=12;
        iResumeTimer(index[2]);
    }
}

void sort_type_change(int what)
{
    if(working)
    {
        working=0;
        pause=1;
        fseek(fp,0,SEEK_SET);
        read();
        description[0]='\0';
        iPauseTimer(index[k]);
    }
    sort_type=what;
}

/*
	function iMouseMove() is called when the user presses and drags the mouse.
	(mx, my) is the position where the mouse pointer is.
	*/

void iMouseMove(int mx, int my)
{
    if(mask(mx,my,549,10,649,40))
        switch_mode[0]=7;
    else
        switch_mode[0]=6;
    if(switch_mode[1]==5)
    {
        if(mask(mx,my,0,520,100,550))//object button
        {
            option_mode[0]=7;
            option_mode[1]=6;
        }

        else if(mask(mx,my,0,490,100,520))//order button
        {
            option_mode[0]=6;
            option_mode[1]=7;
        }

    }
    if((mx-650)*(mx-650)+(my-65)*(my-65)<=196)//pause/play button
        pause_button_mode=1;
    else
        pause_button_mode=0;

    if(working&&(mx-620)*(mx-620)+(my-65)*(my-65)<=196)//previous button
        prev_button_mode=1;
    else
        prev_button_mode=0;

    if(working&&(mx-680)*(mx-680)+(my-65)*(my-65)<=196)//next button
        nxt_button_mode=1;
    else
        nxt_button_mode=0;

    if(working&&(mx-590)*(mx-590)+(my-65)*(my-65)<=196)//set button
        set_button_mode=1;
    else
        set_button_mode=0;

    if(working&&(mx-710)*(mx-710)+(my-65)*(my-65)<=196)//final button
        final_button_mode=1;
    else
        final_button_mode=0;

    if(speed_mode&&mask(mx,my,760,50,820,80))
        speed_pos=820-mx;

    if(mask(mx,my,1235,550,1298,580))//quit button
        switch_mode[5]=5;
    else
        switch_mode[5]=4;
    //place your codes here
}

/*
	function iMouse() is called when the user presses/releases the mouse.
	(mx, my) is the position where the mouse pointer is.
	*/
void iMouse(int button, int state, int mx, int my)
{
    if (button == GLUT_LEFT_BUTTON)
    {
        //place your codes here
        if(state== GLUT_DOWN&&mask(mx,my,0,550,65,580))//sort button
        {
            switch_mode[1]^=4^5;
            if(switch_mode[1]==4)
            {
                option_mode[0]=6;
                option_mode[1]=6;
            }
        }
        else if(state== GLUT_DOWN&&switch_mode[1]==5&&mask(mx,my,0,520,100,550))//object button
        {
            option_mode[0]=7;
            option_mode[1]=6;
        }


        else if(switch_mode[1]==5&&option_mode[0]==7&&mask(mx,my,101,520,201,549))//number button
        {
            option_mode[1]=6;
            //switch_mode[1]=4;
            //auto_generate=1;
            if(!sort_what)
            {
                sort_what=1;
                reset_bubble();
            }

        }
        else if(switch_mode[1]==5&&option_mode[0]==7&&mask(mx,my,101,490,201,519))//string button
        {
            option_mode[1]=6;
            //switch_mode[1]=4;
            //auto_generate=0;
            if(sort_what)
            {
                sort_what=0;
                reset_bubble();
            }

        }


        else if(state== GLUT_DOWN&&switch_mode[1]==5&&mask(mx,my,0,490,100,520))//order button
        {
            option_mode[1]=7;
            option_mode[0]=6;
        }

        else if(switch_mode[1]==5&&option_mode[1]==7&&mask(mx,my,101,520,281,549))//ascending button
        {
            option_mode[0]=6;

            if(order&&working)
            {
                sort_type_change(sort_what);
            }
            order=0;
        }
        else if(switch_mode[1]==5&&option_mode[1]==7&&mask(mx,my,101,490,281,519))//descending button
        {
            option_mode[0]=6;

            if(!order&&working)
            {
                sort_type_change(sort_what);
            }
            order=1;
        }
        else if(switch_mode[1]==5&&option_mode[1]==7&&mask(mx,my,101,460,281,489)&&sort_what==1)//lexicographical button
        {
            option_mode[0]=6;
            lex^=1;
            if(working)
                sort_type_change(sort_what);
        }
        else if(state== GLUT_DOWN)
        {
            switch_mode[1]=4;
            option_mode[0]=6;
            option_mode[1]=6;
        }


        if(mask(mx,my,65,550,140,580))//input button
        {
            if(state==GLUT_DOWN)switch_mode[2]^=4^5;
        }
        else if(switch_mode[2]==5)
        {
            if(mask(mx,my,65,520,165,550))//auto button
            {
                switch_mode[2]=4;
                if(!auto_generate)
                {
                    sort_what=1;
                    auto_generate=1;
                    reset_bubble();
                }
            }
            else if(mask(mx,my,65,490,165,520))//manual button
            {
                switch_mode[2]=4;
                if(auto_generate)
                {
                    auto_generate=0;
                    reset_bubble();
                }
            }
            else
                switch_mode[2]=4;
        }
        if(mask(mx,my,0,50,149,80)&&state == GLUT_DOWN)//description button
        {
            switch_mode[4]^=1;
        }
        if(mask(mx,my,1165,550,1235,580)&&state == GLUT_DOWN)//color button
        {
            switch_mode[3]^=4^5;
        }
        if(mask(mx,my,410,550,499,600)&&state == GLUT_DOWN&&sort_type)//bubble button
        {
            sort_type_change(0);
        }
        if(mask(mx,my,500,550,609,600)&&state == GLUT_DOWN&&sort_type!=1)//selection button
        {
            sort_type_change(1);
        }
        if(mask(mx,my,610,550,719,600)&&state == GLUT_DOWN&&sort_type!=2)//insertion button
        {
            sort_type_change(2);
        }
        if(mask(mx,my,720,550,804,600)&&state == GLUT_DOWN&&sort_type!=3)//merge button
        {
            sort_type_change(3);
        }
        if(mask(mx,my,805,550,890,600)&&state == GLUT_DOWN&&sort_type!=4)//quick button
        {
            sort_type_change(4);
        }
        if(mask(mx,my,1235,550,1298,580))//quit button
        {
            if(state==GLUT_DOWN)
                switch_mode[5]=5;
            else
            {
                //switch_mode[5]=4;
                fclose(fp);
                unlink("file");
                exit(0);
            }
        }
        if(mask(mx,my,549,10,649,40))//reset button
        {
            if(state == GLUT_DOWN)
                switch_mode[0]=7;
            else
            {
                switch_mode[0]=6;
                reset_bubble();
            }
        }

        if(!working&&(bubble_i+bubble_j)&&mask(mx,my,651,10,751,40))//simulate button
        {
            iPauseTimer(index[2]);
            if(bubble[bubble_i].data[bubble_j-1]=='-')
            {
                bubble_j--;
            }
            else if(sort_what&&bubble[bubble_i].data[bubble_j-1]=='.')
            {
                bubble_j--;
                bubble[bubble_i].dx-=size_punc;
                bubble[bubble_i].data[bubble_j]='\0';
            }
            if(auto_generate||!manual_reset);
            else
            {
                if(!bubble_j&&bubble_i)
                {
                    bubble_i--;
                    bubble_j=0;
                }
                else
                {
                    bubble[bubble_i].dx-=12;
                    bubble[bubble_i].data[bubble_j]='\0';
                    bubble[bubble_i].length=bubble_j-decpoint;
                }
                if(bubble_i==0&&bubble_j==0)
                {
                    reset_bubble();
                    return;
                }
            }
            if(manual_reset)
            {
                copy_all_bubbles(bubble_saved,bubble);
                if(sort_what)correct_bubble();
            }
            if(!auto_generate)
            {
                scale_bubble();
                max_length();
            }
            if(sort_what&&l<3&&manual_reset)
                column_auto();

            switch(sort_type)
            {
            case 0:
                bubble_sort();
                break;
            case 1:
                selection_sort();
                break;
            case 2:
                insertion_sort();
                break;
            case 3:
                merge_sort();
                break;
            case 4:
                quick_sort();
                break;
            }
            middle=650-(bubble[bubble_i].tx+max+10)/2;
            manual_reset=0;
            working=1;
            pause=0;
            iResumeTimer(index[0]);
        }

        if((mx-650)*(mx-650)+(my-65)*(my-65)<=196)//pause/play button
        {
            if(state==GLUT_DOWN)
                pause_button_mode=1;
            else
            {
                pause_button_mode=0;
                pause_button_func();
            }
        }
        if(working&&(mx-620)*(mx-620)+(my-65)*(my-65)<=196)//previous button
        {
            if(state==GLUT_DOWN)
                prev_button_mode=1;
            else
            {
                prev_button_mode=0;
                if(ftell(fp)!=(sizeof(struct obj)*(bubble_i+1)+2*sizeof(int)+sizeof(description)))
                    prev_button_func();
            }
        }
        if(working&&(mx-680)*(mx-680)+(my-65)*(my-65)<=196)//next button
        {
            if(state==GLUT_DOWN)
                nxt_button_mode=1;
            else
            {
                nxt_button_mode=0;
                nxt_button_func();
            }
        }
        if(working&&(mx-590)*(mx-590)+(my-65)*(my-65)<=196)//set button
        {
            if(state==GLUT_DOWN)
                set_button_mode=1;
            else
            {
                set_button_mode=0;
                set_button_func();
            }
        }
        if(working&&(mx-710)*(mx-710)+(my-65)*(my-65)<=196)//final button
        {
            if(state==GLUT_DOWN)
                final_button_mode=1;
            else
            {
                final_button_mode=0;
                final_button_func();
            }
        }
        if(state==GLUT_DOWN&&!auto_generate&&(mx-560)*(mx-560)+(my-65)*(my-65)<=196)//edit button
        {
            edit_button_func();
        }
        if(state==GLUT_DOWN&&(mx-740)*(mx-740)+(my-65)*(my-65)<=196)//speed button
        {
            speed_button^=1;
        }
        else if(speed_button&&state==GLUT_DOWN)
        {
            if(mask(mx,my,760,60,820,70))
            {
                speed_pos=820-mx;
                speed_mode=1;
            }
            else
            {
                speed_mode=0;
                speed_button=0;
            }
        }
    }
    if (button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN)
    {
        //place your codes here
    }

}

/*
	function iKeyboard() is called whenever the user hits a key in keyboard.
	key- holds the ASCII value of the key pressed.
	*/

void set_string(char key)
{
    bubble[bubble_i].data[bubble_j]=key;
    bubble_j++;
    if(!bubble[bubble_i].data[bubble_j])
        bubble[bubble_i].data[bubble_j+1]='\0';
    crsr=bubble[bubble_i].data[bubble_j]^'_';

}

void iKeyboard(unsigned char key)

{
    //place your codes for other keys here
    if(switch_mode[1]==5)
        switch_mode[1]=4;
    else if(switch_mode[2]==5)
        switch_mode[2]=4;
    if(working&&key==' ')
        pause_button_func();
    else if(auto_generate||!manual_reset);
    else if(key==8&&bubble_i+bubble_j>0)
    {
        if(bubble_j>0)
            bubble_j--;
        else if(bubble_i>0)
        {
            bubble_i--;
            bubble_j=bubble[bubble_i].length;
            bubble[bubble_i].dx+=28;
        }
        if(bubble[bubble_i].data[bubble_j]>='a'&&bubble[bubble_i].data[bubble_j]<='z')
            bubble[bubble_i].dx-=size_char[0][bubble[bubble_i].data[bubble_j]-'a'];
        else if(bubble[bubble_i].data[bubble_j]>='A'&&bubble[bubble_i].data[bubble_j]<='Z')
            bubble[bubble_i].dx-=size_char[1][bubble[bubble_i].data[bubble_j]-'A'];
        else if(bubble[bubble_i].data[bubble_j]>='0'&&bubble[bubble_i].data[bubble_j]<='9')
            bubble[bubble_i].dx-=size_no;
        else if(bubble[bubble_i].data[bubble_j]=='.')
        {
            bubble[bubble_i].dx-=size_punc;
            decpoint=0;
        }
        else
        {
            bubble[bubble_i].dx-=14;
            negmark=0;
        }
        bubble[bubble_i].data[bubble_j]='\0';
        bubble[bubble_i].data[bubble_j+1]='\0';
    }
    else if(bubble[bubble_i].tx+bubble[bubble_i].dx+20>=1300);
    else if(!sort_what&&key>='a'&&key<='z')
    {
        if(crsr=='_')
            bubble[bubble_i].dx+=size_char[0][key-'a'];
        set_string(key);
    }
    else if(!sort_what&&key>='A'&&key<='Z')
    {
        if(crsr=='_')
            bubble[bubble_i].dx+=size_char[1][key-'A'];
        set_string(key);
    }
    else if(key>='0'&&key<='9')
    {
        if(crsr=='_')
            bubble[bubble_i].dx+=size_no;
        set_string(key);
    }
    else if(key=='.'&&(sort_what&&!decpoint||!sort_what))
    {
        if(crsr=='_')
            bubble[bubble_i].dx+=size_punc;
        set_string(key);
        decpoint=1;
    }
    else if(sort_what&&!negmark&&bubble_j==0&&key=='-')
    {
        if(crsr=='_')
            bubble[bubble_i].dx+=14;
        set_string(key);
        negmark=1;
    }
    else if(bubble_j&&(key=='\r'||key==' '))
    {
        bubble[bubble_i].data[bubble_j]='\0';
        bubble[bubble_i].length=bubble_j-decpoint;
        bubble[bubble_i].dx-=14;
        bubble_i++;
        bubble_j=0;
        negmark=0;
        decpoint=0;
        bubble[bubble_i].data[0]='\0';
        bubble[bubble_i].data[1]='\0';
        bubble[bubble_i].ty=190;
        bubble[bubble_i].mode=0;
        bubble[bubble_i].dx=14;
        bubble[bubble_i].dy=30;
        bubble[bubble_i].length=0;
        bubble[bubble_i].tx=bubble[bubble_i-1].tx+bubble[bubble_i-1].dx+10;
    }

}

/*
	function iSpecialKeyboard() is called whenver user hits special keys like-
	function keys, home, end, pg up, pg down, arraows etc. you have to use
	appropriate constants to detect them. A list is:
	GLUT_KEY_F1, GLUT_KEY_F2, GLUT_KEY_F3, GLUT_KEY_F4, GLUT_KEY_F5, GLUT_KEY_F6,
	GLUT_KEY_F7, GLUT_KEY_F8, GLUT_KEY_F9, GLUT_KEY_F10, GLUT_KEY_F11, GLUT_KEY_F12,
	GLUT_KEY_LEFT, GLUT_KEY_UP, GLUT_KEY_RIGHT, GLUT_KEY_DOWN, GLUT_KEY_PAGE UP,
	GLUT_KEY_PAGE DOWN, GLUT_KEY_HOME, GLUT_KEY_END, GLUT_KEY_INSERT
	*/

void iSpecialKeyboard(unsigned char key)
{
    if(switch_mode[1]==5)
        switch_mode[1]=4;
    else if(switch_mode[2]==5)
        switch_mode[2]=4;

    if (key == GLUT_KEY_END)
    {
        fclose(fp);
        unlink("file");
        exit(0);
    }
    else if(manual_reset||!working);

    else if(key == GLUT_KEY_LEFT&&ftell(fp)!=sizeof(struct obj)*(bubble_i+1)+2*sizeof(int))
        prev_button_func();
    else if(key == GLUT_KEY_RIGHT)
        nxt_button_func();
    else if(key == GLUT_KEY_DOWN)
        set_button_func();
    else if(key == GLUT_KEY_UP)
        final_button_func();
    //place your codes for other keys here
}

int check_bubble(char str1[],char str2[])
{
    if((!sort_what||lex)&&strcmp(str1,str2)<0)
        return 0;
    else if((!sort_what||lex)&&strcmp(str1,str2)==0)
        return !order;
    if(!lex&&sort_what&&atof(str1)<atof(str2))
        return 0;
    else if(!lex&&sort_what&&atof(str1)==atof(str2))
        return !order;
    return 1;
}

void copy_bubble(struct obj *a, struct obj *b)
{
    strcpy(a->data,b->data);
    a->dx=b->dx;
    a->length=b->length;
    a->ty=b->ty;
    a->mode=b->mode;
    a->dy=b->dy;
}

void swap_bubble(struct obj *a, struct obj *b)
{
    copy_bubble(&temp,a);
    copy_bubble(a,b);
    copy_bubble(b,&temp);
}

void write(int a, int b)
{
    excng[0]=a,excng[1]=b;
    fwrite(bubble,sizeof(struct obj),bubble_i+1,fp);
    fwrite(excng,sizeof(int),2,fp);
    fwrite(description,sizeof(description),1,fp);
}

void read()
{
    fread(bubble,sizeof(struct obj),bubble_i+1,fp);
    fread(excng,sizeof(int),2,fp);
    fread(description,sizeof(description),1,fp);
}

void bubble_sort()
{
    int xi,xj;
    fp=fopen("file","wb");
    sprintf(description,"Bubble Sort Initiated");
    write(-1,-1);
    for(xi=0; xi<bubble_i; xi++)
    {
        bubble[0].mode=1;
        for(xj=1; xj<=bubble_i-xi; xj++)
        {
            bubble[xj].mode=1;
            if(order)
                sprintf(description,"%s < %s ? ",bubble[xj-1].data,bubble[xj].data);
            else
                sprintf(description,"%s > %s ? ",bubble[xj-1].data,bubble[xj].data);
            if(!order^check_bubble(bubble[xj].data,bubble[xj-1].data))
            {
                strcat(description,"True");
                write(-1,-1);
                bubble[xj].mode=3;
                bubble[xj-1].mode=3;
                sprintf(description,"Swap %s, %s",bubble[xj-1].data,bubble[xj].data);
                write(xj-1,xj);
                swap_bubble(&bubble[xj],&bubble[xj-1]);
                sprintf(description,"Swap %s, %s",bubble[xj].data,bubble[xj-1].data);
                write(-1,-1);
                bubble[xj].mode=1;
            }
            else
            {
                strcat(description,"False");
                write(-1,-1);
            }
            bubble[xj-1].mode=0;
        }
        bubble[bubble_i-xi].mode=2;
        sprintf(description,"%s Sorted",bubble[bubble_i-xi].data);
        write(-1,-1);
    }
    for(xj=0; xj<=bubble_i; xj++)
    {
        bubble[xj].mode=0;
    }
    sprintf(description,"Successfully Sorted");
    write(-1,-1);
    fclose(fp);
    fp=fopen("file","rb");
    read();
    fseek(fp,0,SEEK_SET);
}

void selection_sort()
{
    int xi,xj;
    fp=fopen("file","wb");
    sprintf(description,"Selection Sort Initiated");
    write(-1,-1);
    for(xi=0; xi<bubble_i; xi++)
    {
        selectn[xi]=xi;
        bubble[xi].mode=3;
        if(order)
            sprintf(description,"Let %s be Maximum",bubble[xi].data);
        else
            sprintf(description,"Let %s be Minimum",bubble[xi].data);
        write(-1,-1);
        for(xj=xi+1; xj<=bubble_i; xj++)
        {
            bubble[xj].mode=1;
            if(order)
                sprintf(description,"%s > %s ? ",bubble[xj].data,bubble[selectn[xi]].data);
            else
                sprintf(description,"%s < %s ? ",bubble[xj].data,bubble[selectn[xi]].data);
            if(!order^check_bubble(bubble[xj].data,bubble[selectn[xi]].data))
            {
                strcat(description,"True");
                write(-1,-1);
                bubble[selectn[xi]].mode=0;
                selectn[xi]=xj;
                bubble[selectn[xi]].mode=3;
                if(order)
                    sprintf(description,"%s is New Maximum",bubble[selectn[xi]].data);
                else
                    sprintf(description,"%s is New Minimum",bubble[selectn[xi]].data);
                write(-1,-1);
            }
            else
            {
                strcat(description,"False");
                write(-1,-1);
                bubble[xj].mode=0;
            }
        }
        bubble[xi].mode=3;
        if(xi!=selectn[xi])
        {
            sprintf(description,"Swap %s, %s",bubble[xi].data,bubble[selectn[xi]].data);
            write(xi,selectn[xi]);
            swap_bubble(&bubble[xi],&bubble[selectn[xi]]);
        }
        write(-1,-1);
        bubble[selectn[xi]].mode=0;
        bubble[xi].mode=2;
        sprintf(description,"%s Sorted",bubble[xi].data);
        write(-1,-1);
    }
    for(xj=0; xj<=bubble_i; xj++)
    {
        bubble[xj].mode=0;
    }
    sprintf(description,"Successfully Sorted");
    write(-1,-1);
    fclose(fp);
    fp=fopen("file","rb");
    read();
    fseek(fp,0,SEEK_SET);
}

void insertion_sort()
{
    int xi,xj;
    fp=fopen("file","wb");
    sprintf(description,"Insertion Sort Initiated");
    write(-1,-1);
    bubble[0].mode=2;
    write(-1,-1);
    for(xi=1; xi<=bubble_i; xi++)
    {
        bubble[xi].mode=3;
        sprintf(description,"Copy %s to Temporary Variable",bubble[xi].data);
        write(xi,-1);
        bubble[xi].ty+=180;
        write(-1,-1);

        for(xj=xi-1; xj>=0; xj--)
        {
            bubble[xj].mode=1;
            if(order)
                sprintf(description,"%s > %s ? ",bubble[xj].data,bubble[xj+1].data);
            else
                sprintf(description,"%s < %s ? ",bubble[xj].data,bubble[xj+1].data);


            if(!order^check_bubble(bubble[xj+1].data,bubble[xj].data))
            {
                strcat(description,"True");
                write(-1,-1);
                bubble[xj].mode=8;
                sprintf(description,"Copy %s to Position %d",bubble[xj].data,xj+1);
                write(xj,xj+1);
                swap_bubble(&bubble[xj+1],&bubble[xj]);
                write(-1,-1);
                bubble[xj+1].mode=2;
            }
            else
            {
                strcat(description,"False");
                write(-1,-1);
                bubble[xj].mode=2;
                break;
            }
        }
        sprintf(description,"Copy back %s to Position %d",bubble[xi].data,xj+1);
        write(xj+1,-1);
        bubble[xj+1].mode=2;
        bubble[xj+1].ty-=180;
        sprintf(description,"%s Sorted",bubble[xj+1].data);
        write(-1,-1);

    }
    for(xj=0; xj<=bubble_i; xj++)
    {
        bubble[xj].mode=0;
    }
    sprintf(description,"Successfully Sorted");
    write(-1,-1);
    fclose(fp);
    fp=fopen("file","rb");
    read();
    fseek(fp,0,SEEK_SET);
}

void merge(struct obj arr[], int l, int m, int r)
{
    int i, j, k;
    int n1 = m - l + 1;
    int n2 =  r - m;
    /* create temp arrays */
    struct obj L[n1], R[n2];

    /* Copy data to temp arrays L[] and R[] */
    for (i = 0; i < n1; i++)
    {
        copy_bubble(&L[i],&arr[l + i]);
        L[i].tx=arr[l + i].tx;
        L[i].mode=2;
        arr[l+i].mode=1;
    }

    for (j = 0; j < n2; j++)
    {
        copy_bubble(&R[j],&arr[m + 1+ j]);
        R[j].tx=arr[m+1+j].tx;
        R[j].mode=2;
        arr[m+1+j].mode=8;
    }
    sprintf(description,"Two Sorted Sub-arrays Taken (%d-%d), (%d-%d)",l,m,m+1,r);
    write(-1,-1);

    /* Merge the temp arrays back into arr[l..r]*/
    i = 0; // Initial index of first subarray
    j = 0; // Initial index of second subarray
    k = l; // Initial index of merged subarray
    while (i < n1 && j < n2)
    {
        if(order)
            sprintf(description,"%s >= %s ? ",L[i].data,R[j].data);
        else
            sprintf(description,"%s <= %s ? ",L[i].data,R[j].data);
        if (!order^!check_bubble(R[j].data, L[i].data))
        {
            strcat(description,"True: Copy ");
            strcat(description,L[i].data);
            strcat(description," to Temporary Array");
            arr[l+i].mode=3;
            write(l+i,(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5);
            arr[l+i].tx=(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5;
            arr[l+i].ty+=180;
            write(-1,-1);
            i++;
        }
        else
        {
            strcat(description,"False: Copy ");
            strcat(description,R[i].data);
            strcat(description," to Temporary Array");
            arr[m+1+j].mode=3;
            write(m+1+j,(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5);
            arr[m+1+j].tx=(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5;
            arr[m+1+j].ty+=180;
            write(-1,-1);
            j++;
        }
        k++;
    }
    while (i < n1)
    {
        sprintf(description,"Copy %s to Temporary Array",L[i].data);
        arr[l+i].mode=3;
        write(l+i,(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5);
        arr[l+i].tx=(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5;
        arr[l+i].ty+=180;
        write(-1,-1);
        i++;
        k++;
    }
    while (j < n2)
    {
        sprintf(description,"Copy %s to Temporary Array",R[j].data);
        arr[m+1+j].mode=3;
        write(m+1+j,(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5);
        arr[m+1+j].tx=(k<=m)?L[k-l].tx+5:R[k-m-1].tx-5;
        arr[m+1+j].ty+=180;
        write(-1,-1);
        j++;
        k++;
    }

    i=j=0,k=l;
    sprintf(description,"Copy Temporary Array Back to Position (%d-%d)",l,r);
    while (i < n1 && j < n2)
    {
        if (!order^!check_bubble(R[j].data, L[i].data))
        {
            write(l+i,-1);
            arr[l+i].ty-=180;
            write(-1,-1);
            i++;
        }
        else
        {
            write(m+1+j,-1);
            arr[m+1+j].ty-=180;
            write(-1,-1);
            j++;
        }
        k++;
    }
    while (i < n1)
    {
        write(l+i,-1);
        arr[l+i].ty-=180;
        write(-1,-1);
        i++;
        k++;
    }
    while (j < n2)
    {
        write(m+1+j,-1);
        arr[m+1+j].ty-=180;
        write(-1,-1);
        j++;
        k++;
    }


    for (i = 0; i < n1; i++)
    {
        arr[l + i].tx=L[i].tx;
    }

    for (j = 0; j < n2; j++)
    {
        arr[m+1+j].tx=R[j].tx;
    }
    i=j=0,k=l;
    while (i < n1 && j < n2)
    {
        if (!order^!check_bubble(R[j].data, L[i].data))
        {
            copy_bubble(&arr[k], &L[i]);
            i++;
        }
        else
        {
            copy_bubble(&arr[k], &R[j]);
            j++;
        }
        k++;
    }

    /* Copy the remaining elements of L[], if there
       are any */
    while (i < n1)
    {
        copy_bubble(&arr[k], &L[i]);
        i++;
        k++;
    }

    /* Copy the remaining elements of R[], if there
       are any */
    while (j < n2)
    {
        copy_bubble(&arr[k], &R[j]);
        j++;
        k++;
    }
    for(int i=0; i<=m; i++)
    {
        bubble[i].tx+=5;
    }
    for(int i=m+1; i<=bubble_i; i++)
    {
        bubble[i].tx-=5;
    }
    sprintf(description,"Position (%d-%d) Sorted",l,r);
    write(-1,-1);
}

void mergeSort(struct obj arr[], int l, int r)
{

    if (l < r)
    {
        // Same as (l+r)/2, but avoids overflow for
        // large l and h
        int m = l+(r-l)/2;
        for(int i=0; i<=m; i++)
        {
            bubble[i].tx-=5;
        }
        for(int i=m+1; i<=bubble_i; i++)
        {
            bubble[i].tx+=5;
        }
        sprintf(description,"Divide Sub-array(%d-%d) into two",l,r);
        write(-1,-1);
        // Sort first and second halves
        mergeSort(arr, l, m);
        mergeSort(arr, m+1, r);

        merge(arr, l, m, r);
    }
}

void merge_sort()
{
    int xj;
    fp=fopen("file","wb");
    sprintf(description,"Merge Sort Initiated");
    write(-1,-1);
    mergeSort(bubble,0,bubble_i);
    for(xj=0; xj<=bubble_i; xj++)
    {
        bubble[xj].mode=0;
    }
    sprintf(description,"Successfully Sorted");
    write(-1,-1);
    fclose(fp);
    fp=fopen("file","rb");
    read();
    fseek(fp,0,SEEK_SET);
}

int random_partition(struct obj arr[], int start, int end)
{
    srand(time(NULL));
    int pivotIdx = start + rand() % (end-start+1);
    arr[pivotIdx].mode=9;
    sprintf(description,"In Sub-array (%d-%d) Pivot selected at Position %d",start,end,pivotIdx);
    write(-1,-1);
    if(pivotIdx!=end)
    {
        sprintf(description,"Swap Pivot with Position %d",end);
        write(pivotIdx,end);
    }
    swap_bubble(&arr[pivotIdx], &arr[end]); // move pivot element to the end
    pivotIdx = end;
    sprintf(description,"Pivot at the End position");
    write(-1,-1);
    int i = start -1,j;

    for(j=start; j<=end-1; j++)
    {
        arr[j].mode=1;
        if(order)
            sprintf(description,"%s > %s ? ",arr[j].data, arr[pivotIdx].data);
        else
            sprintf(description,"%s < %s ? ",arr[j].data, arr[pivotIdx].data);
        if(!order^check_bubble(arr[j].data, arr[pivotIdx].data))
        {
            strcat(description,"True");
            write(-1,-1);
            i = i+1;
            if(j!=i)
            {
                sprintf(description,"Swap %s, %s",arr[i].data,arr[j].data);
                write(i,j);
                swap_bubble(&arr[i], &arr[j]);
                write(-1,-1);
            }
            arr[i].mode=10;
        }
        else
        {
            strcat(description,"False");
            write(-1,-1);
            arr[j].mode=3;
        }
    }
    sprintf(description,"Swap %s(Pivot), %s",arr[pivotIdx].data,arr[i+1].data);
    write(i+1,pivotIdx);
    swap_bubble(&arr[i+1], &arr[pivotIdx]);
    write(-1,-1);

    for(j=start; j<=end; j++)
    {
        if(j==i+1)
            arr[j].mode=2;
        else
            arr[j].mode=0;
    }
    sprintf(description,"%s Sorted",arr[i+1].data);
    write(-1,-1);
    return i+1;
}

void random_quick_sort(struct obj arr[], int start, int end)
{
    if(start < end)
    {
        int mid = random_partition(arr, start, end);
        random_quick_sort(arr, start, mid-1);
        random_quick_sort(arr, mid+1, end);
    }
}

void quick_sort()
{
    int xj;
    fp=fopen("file","wb");
    sprintf(description,"Quick Sort Initiated");
    write(-1,-1);
    random_quick_sort(bubble,0,bubble_i);
    for(xj=0; xj<=bubble_i; xj++)
    {
        bubble[xj].mode=0;
    }
    sprintf(description,"Successfully Sorted");
    write(-1,-1);
    fclose(fp);
    fp=fopen("file","rb");
    read();
    fseek(fp,0,SEEK_SET);
}

void swap_object()
{
    vx=2*(excng[1]-excng[0]);
    x=bubble[excng[0]].tx;
    d=bubble[excng[1]].tx-bubble[excng[0]].tx;
    iPauseTimer(index[0]);
    iResumeTimer(index[1]);
    k=1;
}

void animate_sort()
{
    speed++;
    speed%=((int)speed_pos+5);
    if(!speed)
    {
        read();
        if(!feof(fp))
        {
            if(sort_type==3&&excng[0]!=-1)
            {
                if(excng[1]==-1)
                {
                    vx=0;
                    vy=-6;
                    x=190;
                }
                else
                {
                    vx=6*(excng[1]-bubble[excng[0]].tx)/180;
                    vy=6;
                    x=370;
                }
                speed--;
                k=3;
                iPauseTimer(index[0]);
                iResumeTimer(index[3]);
            }
            else if(excng[1]!=-1&&excng[0]!=-1)
            {
                speed--;
                swap_object();
            }

            else if(excng[0]!=-1)
            {
                x=560-bubble[excng[0]].ty;
                vy=6*(x-bubble[excng[0]].ty)/180;
                iPauseTimer(index[0]);
                iResumeTimer(index[3]);
                k=3;
                speed--;
            }
        }
        else
        {
            speed--;
            iPauseTimer(index[0]);
            pause=1;
        }
    }
}

void change_pos()
{
    bubble[excng[0]].tx+=vx*(3-speed_pos/30);
    bubble[excng[1]].tx-=vx*(3-speed_pos/30);
    if(bubble[excng[1]].tx<=x)
    {
        bubble[excng[1]].tx=x;
    }
    if(bubble[excng[0]].tx>=x+d)
    {
        bubble[excng[0]].tx=x+d;
        vx=0;
    }
    if(!vx)
    {
        iPauseTimer(index[1]);
        iResumeTimer(index[0]);
        k=0;
    }
}

void move_pos()
{
    bubble[excng[0]].ty+=vy*(3-speed_pos/30);
    if(sort_type==3&&excng[1]!=-1)
    {
        bubble[excng[0]].tx+=vx*(3-speed_pos/30);
    }

    if(bubble[excng[0]].ty>=x&&vy>0||bubble[excng[0]].ty<=x&&vy<0)
    {
        bubble[excng[0]].ty=x;
        if(sort_type==3&&excng[1]!=-1)
            bubble[excng[0]].tx=excng[1];
        iPauseTimer(index[3]);
        iResumeTimer(index[0]);
        k=0;
    }
}

void cursor()
{
    bubble[bubble_i].data[bubble_j]^=crsr;
}
int main()
{
    //place your own initialization codes here.

    index[0]=iSetTimer(1,animate_sort);
    iPauseTimer(index[0]);
    index[1]=iSetTimer(1,change_pos);
    iPauseTimer(index[1]);
    index[2]=iSetTimer(500,cursor);
    index[3]=iSetTimer(1,move_pos);
    iPauseTimer(index[3]);
    reset_bubble();
    iInitialize(1300, 600, "Sorting Algorithm Simulator");

    return 0;
}
