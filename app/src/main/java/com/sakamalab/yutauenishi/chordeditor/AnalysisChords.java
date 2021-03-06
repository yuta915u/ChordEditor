package com.sakamalab.yutauenishi.chordeditor;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Collections;
import java.util.Comparator;

import java.util.Map.Entry;




public class AnalysisChords extends AppCompatActivity {


    public HashMap<String, Integer> ChordType(String type,HashMap<String, Integer> map,int flag){
        int root=map.get("root");

        if(flag!=0){
            map.put("fraction", flag);
            root=root+12;
            map.put("root", (root)%24);

        }

        switch(type){
            //対応済
            case "add9":
                map.put("sub3", (root+2)%24);break;
            case "aug":
                map.put("sub2", (root+8)%24);break;
            case "dim":
                map.put("sub1", (root+3)%24);map.put("sub2", (root+6)%24);map.put("sub3", (root+9)%24);break;
            case "sus4":
                map.put("sub1", (root+5)%24);break;
            case "m":
                map.put("sub1", (root+3)%24);break;
            case "m6":
                map.put("sub1", (root+3)%24);
                map.put("sub3", (root+9)%24);break;
            case "m7":
                map.put("sub1", (root+3)%24);
                map.put("sub3", (root+10)%24);break;
            case "maj7":case "Maj7":case "M7":
                map.put("sub3", (root+11)%24);break;
            case "mM7":case "m+7":case "mMaj7":
                map.put("sub1", (root+3)%24);
                map.put("sub3", (root+11)%24);break;
            case "6":
                map.put("sub3", (root+9)%24);break;
            case "69":case "6/9":case "6(9)":
                map.put("sub3", (root+9)%24);
                map.put("sub4", (root+2)%24);break;
            case "7":
                map.put("sub3", (root+10)%24);break;
            case "7sus4":
                map.put("sub1", (root+5)%24);
                map.put("sub3", (root+10)%24);break;
            case "m7(b5)":case "m7♭5":case "m7(-5)":case "m7-5":
                map.put("sub1", (root+3)%24);
                map.put("sub2", (root+6)%24);
                map.put("sub3", (root+10)%24);
                break;
            case "7(b5)":case "7♭5":case "7(-5)":case "7-5":
                map.put("sub2", (root+6)%24);
                map.put("sub3", (root+10)%24);
                break;


            //未対応
            case "maj9":case"Maj9":case "M9":break;
            case "dim7":break;
            case "7aug":break;
            case "9":break;
            case "11":break;
            case "M7aug":break;

            default:
        }
        return map;
    }


    //コードのルートを見つけ、和音の構成音を見つける
    public HashMap<String, Integer> ChordNameAnalysis(String chord){
        HashMap<String,Integer> map= new HashMap<>();
        int root;
        String type;
        switch (chord.length()) {
            case 0:
                return null;
            case 1:
                root=ChordToNo(chord.charAt(0));
                map.put("root", root);
                map.put("sub1", (root+4)%24);
                map.put("sub2", (root+7)%24);
                return map;
            default:
                root=ChordToNo(chord.charAt(0));
                if(chord.charAt(1)=='#'){
                    root++;
                    if(chord.length()==2){
                        map.put("root", root);
                        map.put("sub1", (root+4)%24);
                        map.put("sub2", (root+7)%24);
                        return map;
                    }
                    type=chord.substring(2);
                }else{
                    type=chord.substring(1);
                }
                map.put("root", root);
                map.put("sub1", (root+4)%24);
                map.put("sub2", (root+7)%24);

                Matcher m_t = Pattern.compile("/").matcher(type);
                if (m_t.find()) {
                    String[] type_s= type.split("/", 0);
                    if(type_s.length!=2){
                        map=ChordType(type,map,0);
                        Log.i("テスト  ", "ChordNameAnalysis エラー");
                    }
                    int f_n=ChordToNo(type_s[1].charAt(0));
                    switch (type_s[1].length()){
                        case 1:break;
                        case 2:if(type_s[1].charAt(1)=='#')f_n++;break;
                        default:
                    }
                    map=ChordType(type_s[0],map,f_n);
                }else{
                    map=ChordType(type,map,0);
                }



                //map=ChordType(type,map,0);
                return map;
        }
    }


    public String ChordToText(String chord){
        String result;

        result="| "+chord.replaceAll(","," \\| ");
        result = Pattern.compile("(.*?)\\n(.+?)").matcher(result).replaceAll("$1\n\\| $2");

        return result;
    }


    //♭を全部なくし、＃にする
    public String FlatToSharp(String text){

        String s="\\[\\$";
        String f="\\$\\]";
        text = Pattern.compile("\\|(.*?)\\|").matcher(text).replaceAll(s+"$1"+f);

        text = Pattern.compile(s+"(.*?)C♭(.*?)"+f).matcher(text).replaceAll(s+"$1B$2"+f);
        text = Pattern.compile(s+"(.*?)D♭(.*?)"+f).matcher(text).replaceAll(s+"$1C#$2"+f);
        text = Pattern.compile(s+"(.*?)E♭(.*?)"+f).matcher(text).replaceAll(s+"$1D#$2"+f);
        text = Pattern.compile(s+"(.*?)F♭(.*?)"+f).matcher(text).replaceAll(s+"$1E$2"+f);
        text = Pattern.compile(s+"(.*?)G♭(.*?)"+f).matcher(text).replaceAll(s+"$1F#$2"+f);
        text = Pattern.compile(s+"(.*?)A♭(.*?)"+f).matcher(text).replaceAll(s+"$1G#$2"+f);
        text = Pattern.compile(s+"(.*?)B♭(.*?)"+f).matcher(text).replaceAll(s+"$1A#$2"+f);
        text = Pattern.compile(s+"(.*?)B#(.*?)"+f).matcher(text).replaceAll(s+"$1C$2"+f);
        text = Pattern.compile(s+"(.*?)E#(.*?)"+f).matcher(text).replaceAll(s+"$1F$2"+f);
        text = Pattern.compile(s+"(.*?)on(.*?)"+f).matcher(text).replaceAll(s+"$1/$2"+f);
        text = Pattern.compile(s+"(.*?)"+f).matcher(text).replaceAll("\\|$1\\|");
        return text;
    }

    //-,をなくして、1行に4個コードがある状態に統一
    public String UnityRowNumber(String chordsALL) {
        if(chordsALL.equals(""))return "";
        chordsALL=Pattern.compile("-,").matcher(chordsALL).replaceAll("<->");
        return chordsALL;
    }

    //コード一文字目を下記に基づいて数字化する
    public  int ChordToNo(char first){
        /*
        0  C
        1  C#
        2  D
        3  D#
        4  E
        5  F
        6  F#
        7  G
        8  G#
        9  A
        10 A#
        11 B
         */
        switch (first){
            case 'C':
                return 0;
            case 'D':
                return 2;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 7;
            case 'A':
                return 9;
            case 'B':
                return 11;
            default:
                return -1;
        }
    }


    //行 l 列 r　で指定された場所のコードをmapから取り出して返す。
    public String Choose_One(SparseArray<String[]> map,int l,int r) {
        if(map==null)return "";
        l=l-1;
        r=r-1;
        int key = map.keyAt(l);
        String[] chord=map.get(key);
        return chord[r];
    }


    //コード解析するさいの＃など
    public String HalfUpDown(String text,int count){

        while(count<0){//ハーフダウンの場合
            count=12+count;
        }
        String s2="<\\$";
        String f2="\\$>";
        //[]内を変更しないために
        text = Pattern.compile("\\[(.*?)B(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"b"+f2+"$2]");
        text = Pattern.compile("\\[(.*?)A(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"a"+f2+"$2]");
        text = Pattern.compile("\\[(.*?)G(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"g"+f2+"$2]");
        text = Pattern.compile("\\[(.*?)F(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"f"+f2+"$2]");
        text = Pattern.compile("\\[(.*?)E(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"e"+f2+"$2]");
        text = Pattern.compile("\\[(.*?)D(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"d"+f2+"$2]");
        text = Pattern.compile("\\[(.*?)C(.*?)\\]").matcher(text).replaceAll("[$1"+s2+"c"+f2+"$2]");

        for(int i=0; i<count; i++) {
            //まず間違いがあったら訂正する
            text = Pattern.compile("B#").matcher(text).replaceAll("C");
            text = Pattern.compile("E#").matcher(text).replaceAll("F");
            //2回変換することがないように、とりあえずBをいったんXに置き換え
            text = Pattern.compile("B").matcher(text).replaceAll("X");
            text = Pattern.compile("A#").matcher(text).replaceAll("B");
            text = Pattern.compile("A").matcher(text).replaceAll("A#");
            text = Pattern.compile("G#").matcher(text).replaceAll("A");
            text = Pattern.compile("G").matcher(text).replaceAll("G#");
            text = Pattern.compile("F#").matcher(text).replaceAll("G");
            text = Pattern.compile("F").matcher(text).replaceAll("F#");
            text = Pattern.compile("E").matcher(text).replaceAll("F");
            text = Pattern.compile("D#").matcher(text).replaceAll("E");
            text = Pattern.compile("D").matcher(text).replaceAll("D#");
            text = Pattern.compile("C#").matcher(text).replaceAll("D");
            text = Pattern.compile("C").matcher(text).replaceAll("C#");
            //XをCに戻す
            text = Pattern.compile("X").matcher(text).replaceAll("C");
        }

        //[]内を変更しないために
        text = Pattern.compile(s2+"b"+f2).matcher(text).replaceAll("B");
        text = Pattern.compile(s2+"a"+f2).matcher(text).replaceAll("A");
        text = Pattern.compile(s2+"g"+f2).matcher(text).replaceAll("G");
        text = Pattern.compile(s2+"f"+f2).matcher(text).replaceAll("F");
        text = Pattern.compile(s2+"e"+f2).matcher(text).replaceAll("E");
        text = Pattern.compile(s2+"d"+f2).matcher(text).replaceAll("D");
        text = Pattern.compile(s2+"c"+f2).matcher(text).replaceAll("C");




        return text;
    }

    //エディットページの#などの実装
    public String HalfUpDownEdit(String text,int count){

        text=FlatToSharp(text);
        while(count<0){//ハーフダウンの場合
            count=12+count;
        }
        String s="\\[\\$";
        String f="\\$\\]";
        String s2="<\\$";
        String f2="\\$>";

        text = Pattern.compile("::(.*?)::").matcher(text).replaceAll("|"+s2+"$1"+f2+"|");





        text = Pattern.compile("\\|(.*?)\\|").matcher(text).replaceAll(s+"$1"+f);

        for(int i=0; i<count; i++) {
            //まず間違いがあったら訂正する  必要なかった
            //text = Pattern.compile("\\|B#(.*?)\\|").matcher(text).replaceAll("\\|C$1\\|");
            //text = Pattern.compile("\\|E(.*?)\\|").matcher(text).replaceAll("|F");
            //2回変換することがないように、とりあえずBをいったんXに置き換え
            text = Pattern.compile(s+"(.*?)B(.*?)"+f).matcher(text).replaceAll(s+"$1X$2"+f);
            text = Pattern.compile(s+"(.*?)A#(.*?)"+f).matcher(text).replaceAll(s+"$1B$2"+f);
            text = Pattern.compile(s+"(.*?)A(.*?)"+f).matcher(text).replaceAll(s+"$1A#$2"+f);
            text = Pattern.compile(s+"(.*?)G#(.*?)"+f).matcher(text).replaceAll(s+"$1A$2"+f);
            text = Pattern.compile(s+"(.*?)G(.*?)"+f).matcher(text).replaceAll(s+"$1G#$2"+f);
            text = Pattern.compile(s+"(.*?)F#(.*?)"+f).matcher(text).replaceAll(s+"$1G$2"+f);
            text = Pattern.compile(s+"(.*?)F(.*?)"+f).matcher(text).replaceAll(s+"$1F#$2"+f);
            text = Pattern.compile(s+"(.*?)E(.*?)"+f).matcher(text).replaceAll(s+"$1F$2"+f);
            text = Pattern.compile(s+"(.*?)D#(.*?)"+f).matcher(text).replaceAll(s+"$1E$2"+f);
            text = Pattern.compile(s+"(.*?)D(.*?)"+f).matcher(text).replaceAll(s+"$1D#$2"+f);
            text = Pattern.compile(s+"(.*?)C#(.*?)"+f).matcher(text).replaceAll(s+"$1D$2"+f);
            text = Pattern.compile(s+"(.*?)C(.*?)"+f).matcher(text).replaceAll(s+"$1C#$2"+f);
            //XをCに戻す
            text = Pattern.compile(s+"(.*?)X(.*?)"+f).matcher(text).replaceAll(s+"$1C$2"+f);

        }
        text = Pattern.compile(s+"(.*?)"+f).matcher(text).replaceAll("\\|$1\\|");
        text = Pattern.compile("\\|"+s2+"(.*?)"+f2+"\\|").matcher(text).replaceAll("::$1::");

        return text;
    }


    //歌詞+コードからコードだけを抜き出す。
    public String GetChords(String text){
        String chords;
        String chordsALL = "";
        text=FlatToSharp(text);


        Pattern p1 = Pattern.compile("\\|.*?\\|");
        if(text!=null) {
            text=Pattern.compile("\\[(.*?)\\]").matcher(text).replaceAll("\\|\\[$1\\]\\|");
            text=Pattern.compile("\uFEFF").matcher(text).replaceAll("");
            Matcher ma=Pattern.compile("\\{(?:.|\\n)*?\\}").matcher(text);
            while (ma.find()){
                Matcher m2=Pattern.compile("\\n").matcher(ma.group());
                String new_text=ma.group();
                while (m2.find()){
                    new_text=Pattern.compile("\\n").matcher(new_text).replaceAll("");
                }
                text = text.replace(ma.group(),new_text);
            }

            text=Pattern.compile("::(?:.|\\n)*?::").matcher(text).replaceAll("-,");
            text=Pattern.compile("\\|:(.*?):\\|").matcher(text).replaceAll("|$1,$1|");
            String[] data_split = text.split("\\n", 0);
            for (String data_s : data_split) {
                chords="";
                Matcher m1 = p1.matcher(data_s);
                while (m1.find()){
                    chords=chords+m1.group().substring(1, m1.group().length() - 1)+",";
                }
                if(!(chords.equals("")))chordsALL=chordsALL+chords+"\n";
            }
        }

        return chordsALL;
    }



    //コードのルートを見つける
    public String GetChordRoot(String chord){
        String root;
        chord=chord.replaceAll("^/(.*?)","");//分数コードの場合、分母をルートと見なすため、/より後ろを消す
        chord=chord.replaceAll("^on(.*?)","");//分数コードの場合、分母をルートと見なすため、/より後ろを消す
        if(chord.equals(""))return "";
        switch (chord.length()) {
            case 1:
                return chord;
            case 2:
                root=chord.substring(0, 1);
                if(chord.charAt(1)=='#'||chord.charAt(1)=='m'){
                    root=chord;
                }
                return root;
            case 3:case 4:
                if(chord.charAt(1)=='#'){
                    root=chord.substring(0,2);
                    if(chord.charAt(2)=='m'){
                        root=chord.substring(0,3);
                    }
                }else{
                    root=chord.substring(0,1);
                    if(chord.charAt(1)=='m'){
                        root=chord.substring(0,2);
                    }
                }
                return root;
            default:
                if(chord.charAt(1)=='#'){
                    root=chord.substring(0, 2);
                    if(chord.charAt(2)=='m'&&!(chord.charAt(3)=='a'&&chord.charAt(4)=='j')){
                        root=chord.substring(0, 3);
                    }
                }else{
                    root=chord.substring(0, 1);
                    if(chord.charAt(1)=='m'&&!(chord.charAt(2)=='a'&&chord.charAt(3)=='j')){
                        root=chord.substring(0, 2);
                    }
                }
                return root;
        }
    }






    //コードのルートを見つける
    public String GetChordRootPlus(String chord){
        String root,sub_root;
        sub_root="";
        String[] c_s=chord.split("/", 0);
        if(c_s.length>1){
            sub_root="/"+c_s[1];
        }

        chord=chord.replaceAll("^/(.*?)","");//分数コードの場合、分母をルートと見なすため、/より後ろを消す
        chord=chord.replaceAll("^on(.*?)","");//分数コードの場合、分母をルートと見なすため、/より後ろを消す
        if(chord.equals(""))return "";
        switch (chord.length()) {
            case 1:
                return chord+sub_root;
            case 2:
                root=chord.substring(0, 1);
                if(chord.charAt(1)=='#'||chord.charAt(1)=='m'){
                    root=chord;
                }
                return root+sub_root;
            case 3:case 4:
                if(chord.charAt(1)=='#'){
                    root=chord.substring(0,2);
                    if(chord.charAt(2)=='m'){
                        root=chord.substring(0,3);
                    }
                }else{
                    root=chord.substring(0,1);
                    if(chord.charAt(1)=='m'){
                        root=chord.substring(0,2);
                    }
                }
                return root+sub_root;
            default:
                if(chord.charAt(1)=='#'){
                    root=chord.substring(0, 2);
                    if(chord.charAt(2)=='m'&&!(chord.charAt(3)=='a'&&chord.charAt(4)=='j')){
                        root=chord.substring(0, 3);
                    }
                }else{
                    root=chord.substring(0, 1);
                    if(chord.charAt(1)=='m'&&!(chord.charAt(2)=='a'&&chord.charAt(3)=='j')){
                        root=chord.substring(0, 2);
                    }
                }
                return root+sub_root;
        }
    }






    /*//コードのルート以外を見つける
    public String GetChordsType(String chord){
        String sub;
        if(chord.equals(""))return "";
        switch (chord.length()) {
            case 1:
                return "";
            case 2:
                sub=chord.substring(1);
                if(chord.charAt(1)=='#'||chord.charAt(1)=='m'){
                    sub="";
                }
                return sub;
            case 3:case 4:
                if(chord.charAt(1)=='#'){
                    sub=chord.substring(2);
                    if(chord.charAt(2)=='m'){
                        sub=chord.substring(3);
                    }
                }else{
                    sub=chord.substring(1);
                    if(chord.charAt(1)=='m'){
                        sub=chord.substring(2);
                    }
                }
                return sub;
            default:
                if(chord.charAt(1)=='#'){
                    sub=chord.substring(2);
                    if(chord.charAt(2)=='m'&&!(chord.charAt(3)=='a'&&chord.charAt(4)=='j')){
                        sub=chord.substring(3);
                    }
                }else{
                    sub=chord.substring(1);
                    if(chord.charAt(1)=='m'&&!(chord.charAt(2)=='a'&&chord.charAt(3)=='j')){
                        sub=chord.substring(2);
                    }
                }
                return sub;

        }
    }
    */


    //targetの文字がchordsで何回つかわれているか数える
    public int Counter(String chords,String target,int f){//f=1完全一致f=0頭一致
        int num=0;

        if(chords!=null) {
            chords=Pattern.compile("\\n").matcher(chords).replaceAll("");
            chords=Pattern.compile("\\[.*?\\],").matcher(chords).replaceAll("");
            if(chords==null)return 0;
            String[] data_split = chords.split(",", 0);
            if(target.equals(""))return data_split.length;
            Pattern p_t;
            switch (f){
                case 0:
                    p_t= Pattern.compile("^" + target + ".*");
                    break;
                case 1:
                    p_t= Pattern.compile("^" + target + "$");
                    break;
                default:
                    return num;
            }
            for(String data_s : data_split){
                Matcher m_t = p_t.matcher(data_s);
                if (m_t.find())num++;
            }
        }
        return num;
    }


    //全体のコード数に対して、ダイアトニックコードが半分もなかった場合のみ　0以外の値を返す　返す値はキーの
    public int CheckPartKey(String chords){
        int return_i=0;
        int C,Dm,Em,F,G,Am;
        int sum;
        int c_num_all;


        c_num_all=Counter(chords,"",0);
        C = Counter(chords, "C",0) - Counter(chords, "C#",0);
        Dm = Counter(chords, "Dm",0);
        Em = Counter(chords, "Em",0);
        F = Counter(chords, "F",0) - Counter(chords, "F#",0);
        G = Counter(chords, "G",0) - Counter(chords, "G#",0);
        Am = Counter(chords, "Am",0);
        sum = C + Dm + Em + F + G + Am;

        if(sum<c_num_all/2){
            return_i=FindKey(chords);
        }

        return return_i;
    }


    //String Key[] = {"C", "C#","D","D#","E","F","F#","G","G#","A","A#","B"};
    //曲のキーを返す
    public int FindKey(String chords){
        int return_i;
        int C,Dm,Em,F,G,Am;
        int sum[] = {0, 0, 0, 0, 0,0,0,0,0,0,0,0};
        int max=0;
        int max_i=-1;
        int sub_max=0;
        for(int i=0;i<12;i++) {
            C = Counter(chords, "C",0) - Counter(chords, "C#",0);
            Dm = Counter(chords, "Dm",0);
            Em = Counter(chords, "Em",0);
            F = Counter(chords, "F",0) - Counter(chords, "F#",0);
            G = Counter(chords, "G",0) - Counter(chords, "G#",0);
            Am = Counter(chords, "Am",0);
            sum[i] = C + Dm + Em + F + G + Am;



            if(max<sum[i]){
                max=sum[i];
                max_i=i;
            }else if(max==sum[i]){
                sub_max=sum[i];
            }
            chords=HalfUpDown(chords,-1);
        }
        if(sub_max==max){
            //解析不能
            return_i=20;
        }else{
            return_i=max_i;//12-max_iカポするとCで弾ける=
        }

        return return_i;
    }

    //KeyをCにする
    public String ChangeKeyToC(String chords){
        String result_s;
        int num=FindKey(chords);
        result_s = HalfUpDown(chords, -num);
        return result_s;
    }


    //コードデータをStringからSparseArrayに変更
    public SparseArray<String[]> StringToMap(String chordsALL) {

        if(chordsALL.equals(""))return null;
        chordsALL=Pattern.compile("\\[.*?\\],").matcher(chordsALL).replaceAll("");

        String[] data_line = chordsALL.split("\\n", 0);
        SparseArray<String[]> map = new SparseArray<>();


        for(int i=0;i<data_line.length;i++){
            if(!(data_line[i].equals(""))&&!(data_line[i].equals(","))) {
                String[] data_chord = data_line[i].split(",", 0);
                if(data_chord.length!=0)map.put(i,data_chord);
            }
        }


        return map;
    }

    //コードデータをSparseArrayからStringに変更
    public String MapToString(SparseArray<String[]> map) {
        String result="";
        if(map==null)return result;

        for(int i=0;i<map.size();i++){
            int key = map.keyAt(i);
            String[] chord=map.get(key);
            for(String FE_chord:chord){
                result=result+FE_chord+",";
            }
            result=result+"\n";
        }
        return result;
    }


    //Aメロを抜き出す
    public String SelectPart(String chords,String choices) {

        if(chords.equals(""))return "";
        String[] SP_chords1 = chords.split("\\["+choices+"\\],*(\\n)*", 0);
        if(SP_chords1.length<2)return "";
        String[] SP_chords2 = SP_chords1[1].split("\\[.*?\\],*(\\n)*", 0);
        if(SP_chords2.length==0)return "";

        return SP_chords2[0];
    }



    //UCの出力を入力し、ランダムで１つのコードを出力
    public String RandomChoice(HashMap<String, Integer> UC){
        String result="";
        ArrayList<String> array = new ArrayList<>();
        int max=0;
        if(!UC.isEmpty()) {
            for (String key : UC.keySet()) {
                Integer n_times = UC.get(key);
                for(int j=0;j<n_times;j++){
                    array.add(key);
                    max++;
                }
            }
            int ran = (int)(Math.random()*(max));
            result=array.get(ran);
        }

        return result;
    }



    //曲で使われているすべてのコードの種類と出現回数
    public HashMap<String, Integer> UsedChord(String chords){
        HashMap<String,Integer> map = new HashMap<>();
        int num;
        int con=0;
        chords=Pattern.compile("\\n").matcher(chords).replaceAll("");
        chords=Pattern.compile("\\[.*?\\],").matcher(chords).replaceAll("");
        if(chords!=null) {
            chords=","+chords;
            while(con<10&&!(Pattern.compile(",").matcher(chords).replaceAll("").trim().equals(""))) {
                con++;
                String[] data_split = chords.split(",", 0);
                if(data_split[1].equals(""))continue;//[0]は""が入ってる。,C,D,E....だから
                num = Counter(chords, data_split[1],1);//完全一致を探す
                map.put(data_split[1], num);
                chords = Pattern.compile(",("+data_split[1]+",)+").matcher(chords).replaceAll(",");
            }
        }
        return map;
    }



    //曲で使われているすべてのコードの種類と出現回数
    public HashMap<String, Integer> UsedChordXXX(String chords){
        HashMap<String,Integer> map = new HashMap<>();
        int num;
        int con=0;
        chords=Pattern.compile("\\n").matcher(chords).replaceAll("");
        chords=Pattern.compile("\\[.*?\\],").matcher(chords).replaceAll("");
        if(chords!=null) {
            chords=","+chords;
            while(con<100&&!(Pattern.compile(",").matcher(chords).replaceAll("").trim().equals(""))) {
                con++;
                String[] data_split = chords.split(",", 0);
                if(data_split[1].equals(""))continue;//[0]は""が入ってる。,C,D,E....だから
                num = Counter(chords, data_split[1],1);//完全一致を探す
                map.put(data_split[1], num);
                chords = Pattern.compile(",("+data_split[1]+",)+").matcher(chords).replaceAll(",");
            }
        }
        HashMap<String,Integer> new_map = new HashMap<>();
        if(!map.isEmpty()) {
            for (String key : map.keySet()) {
                Integer n = map.get(key);
                key=GetChordRootPlus(key);
                if (new_map.isEmpty()) {
                    new_map.put(key, n);
                } else {
                    Integer n_times = new_map.get(key);
                    if (n_times == null) {
                        new_map.put(key, n);
                    } else {
                        new_map.put(key, n_times + n);
                    }
                }
            }
        }


        return new_map;
    }




    //構成の解析
    public SparseArray<String[]> ConstMatrix(String ALL_chords,int num){
        SparseArray<String[]> SA_matrix = new SparseArray<>();

        if (ALL_chords.equals("")||num<0) return null;
        String[] song = ALL_chords.split("<!!>\n", 0);
        for(int i=0;i<song.length;i++){
            String[] LA_s=LineAnalysis(song[i],num);

            SA_matrix.put(i,LA_s);
        }

        return SA_matrix;
    }



    //分数コードかを判断し、ルートを返す。分数コードの場合、分数
    public String fraction(String target){
        String result;
        Matcher m_t = Pattern.compile("/").matcher(target);
        if (m_t.find()){
            String[] tar_s= target.split("/", 0);
            if(tar_s.length!=2){
                Log.i("テスト  ", "fraction エラー");
                return  GetChordRoot(target);
            }
            result = GetChordRoot(tar_s[0])+"/"+GetChordRoot(tar_s[1]);
        }else{
            result = GetChordRoot(target);
        }
        return result;
    }



    //装飾ありなし変更可能！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
    //出現コードの次のコードの割合を記憶
    public HashMap<String, List<String>> NextChordAnalysis(String ALL_songs){


        SparseArray<String[]> chords_map;
        HashMap<String, List<String>> result_map=new HashMap<>();

        if (ALL_songs.equals("")) return null;
        String[] song = ALL_songs.split("<!!>\n", 0);


        for(String chord:song) {

            chords_map=StringToMap(chord);
            if (chords_map == null) continue;
            String target;
            String[] target_str;

            for (int i = 0; i < chords_map.size(); i++) {
                int key = chords_map.keyAt(i);
                target_str = chords_map.get(key);
                for (int j = 0; j < 3; j++) {
                    List<String> target_list;

                    target = fraction(target_str[j]);
                    //Log.i("テスト  ", "target="+target);
                    //target = target_str[j];
                    target_list = result_map.get(target);
                    if (target_list == null) {
                        List<String> new_list = new ArrayList<>();
                        if (!target_str[j + 1].equals(target_str[j])) {
                            new_list.add(fraction(target_str[j + 1]));
                            //new_list.add(target_str[j + 1]);
                            result_map.put(target, new_list);
                        }
                    } else {
                        if (!target_str[j + 1].equals(target_str[j])) {
                            target_list.add(fraction(target_str[j + 1]));
                            //target_list.add(target_str[j + 1]);
                            result_map.put(target, target_list);
                        }
                    }
                }
            }


        }

        return result_map;
    }







    //構成を解析。行を比較して0123などの文字で表す　numは行数、line_nはフィルター。何行のものを調べるかの。
    public String[] LineAnalysis(String chord,int line_n) {
        String[] box;


        if (chord.equals("")) return null;

        String[] split_c = chord.split("\\n", 0);
        int[] result = new int[split_c.length];
        String[] line = new String[split_c.length];
        String[] sum = new String[4];
        if(split_c.length!=line_n || line_n==0)return null;
        String[] new_matrix=new String[line_n];
        for(int i=0;i<line_n;i++)new_matrix[i]="";


        for(int num=0;num<4;num++) {
            sum[num]="";
            for (int i = 0; i < split_c.length; i++) {
                result[i] = 10;
                line[i] = "";
                if (!split_c[i].equals("")) {
                    box = split_c[i].split(",", 0);
                    if (box.length < num) return null;
                    line[i] = GetChordRoot(box[num]);//ルートが同じ場合同じとみなす。line[i] = box[num]だとCとCadd9は別
                } else return null;

            }

            int point = 0;
            for (int i = 0; i < line.length; i++) {
                if (result[i] > i) {
                    result[i] = point;
                    for (int j = i + 1; j < line.length; j++) {
                        if (line[i].equals(line[j])) {
                            result[j] = point;
                        }

                    }
                    point++;
                }
            }

            for (int r_i : result) sum[num] = sum[num] + String.valueOf(r_i);


            String[] sum_box = sum[num].split("", 0);
            for(int x=0;x<line_n;x++){
                new_matrix[x]=new_matrix[x]+sum_box[x+1];
            }

        }




        for(int x=0;x<line_n;x++){
            new_matrix[x]=new_matrix[x]+","+split_c[x].replaceAll(",", "<!>");
        }

        return new_matrix;
    }

    //指定された文字をマップに入れる。もしすでにある要素の場合、カウント(Integer)を増やす
    public HashMap<String, Integer> PutMap(HashMap<String, Integer> PM,String text){
        if (PM.isEmpty()) {
            PM.put(text, 1);
        } else {
            Integer n_times = PM.get(text);
            if (n_times == null) {
                PM.put(text, 1);
            } else {
                PM.put(text, n_times + 1);
            }
        }
        return PM;
    }

    //指定された文字をマップに入れる。もしすでにある要素の場合、カウント(Integer)を増やす String String ver
    public HashMap<String, String> PutMap_SS(HashMap<String, String> PM_SS,String text1,String text2){
        if (PM_SS.isEmpty()) {
            PM_SS.put(text1, text2+",");
        } else {
            String old_txt = PM_SS.get(text1);
            if (old_txt==null) {
                old_txt=text2+",";
                PM_SS.put(text1, old_txt);
            } else {
                old_txt=old_txt+text2+",";
                PM_SS.put(text1,old_txt);
            }
        }
        return PM_SS;
    }



    HashMap<String,String> Change_Map_A = new HashMap<>();//何が何に変わっているかを保存
    HashMap<String, Integer> CM_for_Random_A = new HashMap<>();//0010など、変わってる部分を保存
    HashMap<String,String> Change_Map_B = new HashMap<>();//何が何に変わっているかを保存
    HashMap<String, Integer> CM_for_Random_B = new HashMap<>();//0010など、変わってる部分を保存
    HashMap<String, List<String>> NC_list_map_A= new HashMap<>();//あるコードの次に来るコードを格納


    //numは行数指定 行を比較してox!で行を分別
    public HashMap<String, Integer> SumLineElement(SparseArray<String[]> SA_matrix,String part){
        HashMap<String,Integer> map = new HashMap<>();

        String[] line = new String[10];//いらないだろうけど余分で10個用意しておく
        int i;
        for(i=0;i<line.length;i++){
            line[i]="";
        }

        for(i=0;i<SA_matrix.size();i++){
            int key = SA_matrix.keyAt(i);
            String[] chord=SA_matrix.get(key);
            if(chord==null)continue;
            String sum_s="";


            String save_0[]=new String[5];//最初は0が入るから横4つ分と空き1つ分の5


            for(String Chord_fe:chord){
                String[] s = Chord_fe.split(",", 0);//今、FE_chordには 0101,CGCGのような文字が入っている
                String[] split_num = s[0].split("", 0);//0101を1文字づつsplit_cに入れる
                String[] split_chord = s[1].split("<!>", 0);//CGCGを1文字づつsplit_chordに入れる
                int sum=Integer.parseInt(split_num[1])+Integer.parseInt(split_num[2])+Integer.parseInt(split_num[3])+Integer.parseInt(split_num[4]);


                switch(sum){
                    case 0:
                        //line[x]=line[x]+"0,";
                        //---------------↓↓↓0の部分をセーブしておくsave_0[]=split_chord[]↓↓↓-------------------
                        save_0=split_chord;
                        //---------------↑↑↑どう変わったかをマップに入れる↑↑↑-------------------
                        sum_s=sum_s+"o";
                        break;
                    case 1:
                    case 2:
                        //line[x]=line[x]+"1,";
                        //---------------↓↓↓どう変わったかをマップに入れる↓↓↓-------------------
                        for(int v=1;v<split_num.length;v++){
                            if(Integer.parseInt(split_num[v])==1){
                                if(part.equals("A")) Change_Map_A=PutMap_SS(Change_Map_A,GetChordRoot(save_0[v-1]),GetChordRoot(split_chord[v-1]));
                                if(part.equals("B")) Change_Map_B=PutMap_SS(Change_Map_B,GetChordRoot(save_0[v-1]),GetChordRoot(split_chord[v-1]));
                            }
                        }
                        //---------------↑↑↑どう変わったかをマップに入れる↑↑↑-------------------
                        if(part.equals("A")) CM_for_Random_A=PutMap(CM_for_Random_A,s[0]);
                        if(part.equals("B")) CM_for_Random_B=PutMap(CM_for_Random_B,s[0]);
                        sum_s=sum_s+"b";
                        break;
                    case 3:case 4:case 5:
                        sum_s=sum_s+"x";
                        break;
                    default:
                        sum_s=sum_s+"!";
                }

            }
            map=PutMap(map,sum_s);
        }

        /*for(i=0;i<line.length;i++){
            Line_sum_A.put(i,line[i]);
        }
        */

        return map;
    }



    //1行目を参考に2行目を考える
    public String[] GetNextLine(String Position_Change,String[] First_CP,String part){
        if(Position_Change==null||First_CP==null) {
            Log.i("テスト  ", "GetNextLine return null");
            return null;
        }
        String[] Next_CP=new String[4];
        String[] Change_Position=Position_Change.split("");//0010とかを一文字ずつ分ける
        String str="";
        if(Change_Position[1].equals("0")){
            Next_CP[0]=First_CP[0];
        }else{
            if(part.equals("A"))str=Change_Map_A.get(First_CP[0]);
            if(part.equals("B"))str=Change_Map_B.get(First_CP[0]);
            if(str==null){
                Next_CP[0]=First_CP[0];
            }else {
                String[] CM_txt = str.split(",");
                Next_CP[0] = CM_txt[(int)(Math.random()*(CM_txt.length))];
            }
        }


        if(Change_Position[2].equals("0")){
            Next_CP[1]=First_CP[1];
        }else{
            str="";
            if(part.equals("A"))str=Change_Map_A.get(First_CP[1]);
            if(part.equals("B"))str=Change_Map_B.get(First_CP[1]);
            if(str==null){
                Next_CP[1]=First_CP[1];
            }else {
                String[] CM_txt = str.split(",");
                Next_CP[1] = CM_txt[(int) (Math.random() * (CM_txt.length))];
                if(CM_txt.length>1) {
                    int i=0;
                    while(Next_CP[1].equals(First_CP[2])){
                        Next_CP[1]=CM_txt[(int) (Math.random() * (CM_txt.length))];
                        i++;
                        if(i==20)break;
                    }
                }else{
                    if(Next_CP[1].equals(First_CP[2]))Next_CP[1]=First_CP[1];
                }
            }
        }


        if(Change_Position[3].equals("0")){
            Next_CP[2]=First_CP[2];
        }else{
            str="";
            if(part.equals("A"))str=Change_Map_A.get(First_CP[2]);
            if(part.equals("B"))str=Change_Map_B.get(First_CP[2]);
            if(str==null){
                Next_CP[2]=First_CP[2];
            }else {
                String[] CM_txt = str.split(",");
                Next_CP[2] = CM_txt[(int) (Math.random() * (CM_txt.length))];
                if(CM_txt.length>1) {
                    int i=0;
                    while(Next_CP[2].equals(Next_CP[1])){
                        Next_CP[2]=CM_txt[(int) (Math.random() * (CM_txt.length))];
                        i++;
                        if(i==20)break;
                    }
                }else{
                    if(Next_CP[2].equals(Next_CP[1]))Next_CP[2]=First_CP[2];
                }
            }
        }


        if(Change_Position[4].equals("0")){
            Next_CP[3]=First_CP[3];
        }else{
            str="";
            if(part.equals("A"))str=Change_Map_A.get(First_CP[3]);
            if(part.equals("B"))str=Change_Map_B.get(First_CP[3]);
            if(str==null){
                Next_CP[3]=First_CP[3];
            }else {
                String[] CM_txt = str.split(",");
                Next_CP[3] = CM_txt[(int) (Math.random() * (CM_txt.length))];
            }
        }

        Log.i("テスト  ", "GetNextLine  Finish---"+Next_CP[0]+Next_CP[1]+Next_CP[2]+Next_CP[3]);

        return Next_CP;
    }



    public String[] GetNewFirstLastChord(String ALL_chords,String avoid_f) {

        String[] result=new String [4];
            SparseArray<String[]> map;
            String first_chord = "";
            String last_chord = "";
            if (ALL_chords.equals("")) return null;
            String[] song = ALL_chords.split("<!!>\n", 0);
            for (String chords : song) {
                if(chords.equals(""))continue;
                map = StringToMap(chords);
                //if(map.size()!=line_n)continue;
                first_chord = first_chord + GetChordRootPlus(Choose_One(map, 1, 1)) + ",";
                last_chord = last_chord + GetChordRootPlus(Choose_One(map, map.size(), 4)) + ",";
            }
            HashMap<String, Integer> UC = UsedChordXXX(first_chord);
            result[0] = RandomChoice(UC);
            HashMap<String, Integer> UC2 = UsedChordXXX(last_chord);
            result[1] = RandomChoice(UC2);
            while (avoid_f.equals(result[0])) result[0] = RandomChoice(UC);

            result[2] =UCtoString(UC);//データ比較用
            result[3] =UCtoString(UC2);//データ比較用

        return result;
    }









    //1行目のコード進行生成
    public String[] FirstChordProgression(String f_chord, String l_chord,HashMap<String, List<String>> NC_list_map, HashMap<String, Integer> Length_map,String avoid_l) {

        String[] new_chord_p= new String[4];
        String[] used_chord = {"0","1","2","3"};
        String next_chord="";
        int[] length_i = new int[4];
        int point=0;
        int ran;
        int flag;
        List<String> next_list;
        if(NC_list_map==null||Length_map==null) {
            Log.i("テスト  ", "FCP return null");
            return null;
        }


        String length_s=RandomChoice(Length_map);
        length_i[0]=Integer.parseInt(length_s.substring(0, 1));
        length_i[1]=Integer.parseInt(length_s.substring(1, 2));
        length_i[2]=Integer.parseInt(length_s.substring(2, 3));
        length_i[3]=Integer.parseInt(length_s.substring(3, 4));


        new_chord_p[0]=f_chord;
        new_chord_p=Check_FCP(length_i,new_chord_p,0,f_chord);
        used_chord[point]=f_chord;
        point++;

        Log.i("テスト  ", "パターンは"+length_s);

        for(int j=1;j<4;j++){
            if(length_i[j]!=point)continue;
            next_list=NC_list_map.get(fraction(new_chord_p[j-1]));
            if(next_list==null){
                Log.i("テスト  ", "FCP失敗　再帰呼出し");
                return FirstChordProgression(f_chord,l_chord,NC_list_map,Length_map,avoid_l);
            }
            flag=0;
            int count=0;
            while(flag==0){
                flag=1;
                ran = (int)(Math.random()*(next_list.size()));
                next_chord=next_list.get(ran);
                for(int k=0;k<point;k++){
                    if(next_chord.equals(used_chord[k]))flag=0;
                }
                if(j==3&&!next_chord.equals(l_chord)&&!avoid_l.equals(l_chord)){
                    flag=0;
                }
                count++;
                if(count>50) {
                    Log.i("テスト  ", "無限ループミス");
                    flag=1;
                }
            }

            new_chord_p[j]=next_chord;
            new_chord_p=Check_FCP(length_i,new_chord_p,j,next_chord);
            used_chord[point]=next_chord;
            point++;
        }
        Log.i("テスト  ", "FirstChordProgression");
        if(new_chord_p[3].equals(avoid_l)){
            Log.i("テスト  ", "もう一度");
            return FirstChordProgression(f_chord,l_chord,NC_list_map,Length_map,avoid_l);
        }
        Log.i("テスト  ",new_chord_p[0]+new_chord_p[1]+new_chord_p[2]+new_chord_p[3]);
        return new_chord_p;
    }

    //コード進行をコピペする際に変わる部分をランダムで生成し、パートの全行のぶんを作る
    public String[] Position_Change_Block(HashMap<String, Integer> CM_for_Random,int new_line_n){
        String[] result=new String[new_line_n];
        if(new_line_n==0)return null;
        result[0]="0000";
        for(int i=1;i<new_line_n;i++){
            result[i]=RandomChoice(CM_for_Random);
        }
        return result;
    }



    public String[] repetition(String allChords_A,String allChords_B,String line_n_A,String line_n_B,String[] Log,int count) {

        String A,B,F_A,F_B,L_A,L_B,Line_A,Line_B;
            A=B=F_A=F_B=L_A=L_B=Line_A=Line_B="";
        for(int i=0;i<count;i++){
            String[] new_CP=GetNewChords(allChords_A,allChords_B,line_n_A,line_n_B);
            A=A+new_CP[0];
            B=B+new_CP[1];
            String[] s1 = new_CP[3].split(";", 0);//行数
            Line_A=Line_A+s1[0]+",";
            Line_B=Line_B+s1[1]+",";
            String[] s2 = new_CP[4].split(";", 0);//最初最後コード
            F_A=F_A+s2[0]+",";
            F_B=F_B+s2[1]+",";
            L_A=L_A+s2[2]+",";
            L_B=L_B+s2[3]+",";
        }


        Log[0]=Log[0]+Line_A;
        Log[1]=Log[1]+Line_B;
        Log[2]=Log[2]+F_A;
        Log[3]=Log[3]+F_B;
        Log[4]=Log[4]+L_A;
        Log[5]=Log[5]+L_B;
        Log[6]=Log[6]+A;
        Log[7]=Log[7]+B;



        return Log;
    }

    public String Data_Open(String[] Log_S) {
        String result="";
        Log.i("テスト  ", "Data_Open 行数解析");
        result=result+"---[Aメロ] 行数解析---\n"+UCtoString(UsedChord(Log_S[0]))+"\n---[Bメロ] 行数解析---\n"+UCtoString(UsedChord(Log_S[1]));
        Log.i("テスト  ", "Data_Open Fコード解析");
        result=result+"\n---[Aメロ] Fコード解析---\n"+UCtoString(UsedChord(Log_S[2]))+"\n---[Bメロ] Fコード解析---\n"+UCtoString(UsedChord(Log_S[3]));
        Log.i("テスト  ", "Data_Open Lコード解析");
        result=result+"\n---[Aメロ] Lコード解析---\n"+UCtoString(UsedChord(Log_S[4]))+"\n---[Bメロ] Lコード解析---\n"+UCtoString(UsedChord(Log_S[5]));
        Log.i("テスト  ", "Data_Open コード出現率");
        result=result+"\n---[Aメロ] コード出現率---\n"+UCtoString(UsedChordXXX(Log_S[6]))+"\n---[Bメロ] コード出現率---\n"+UCtoString(UsedChordXXX(Log_S[7]));
        return result;
    }





    //曲全体のコードを決める
    public String[] GetNewChords(String allChords_A,String allChords_B,String line_n_A,String line_n_B) {

        SparseArray<String[]> new_chord_map_A = new SparseArray<>();
        SparseArray<String[]> new_chord_map_B = new SparseArray<>();


        String Data="";


        //----------↓Aメロ構成の特徴解析↓----------
        int new_line_n_A=0;
        if(!line_n_A.equals(""))new_line_n_A=Integer.parseInt(RandomChoice(UsedChord(line_n_A)));//new_line_n_Aは新しいコード進行の行数
        SparseArray<String[]> SA_matrix_A =ConstMatrix(allChords_A,new_line_n_A);
        String A_Const=RandomChoice(SumLineElement(SA_matrix_A,"A"));//sumを使って 行を比較してox!で行を分別 oxox oooo oxoo ox oo
        NC_list_map_A =NextChordAnalysis(allChords_A);//コードの連結を調べる
        HashMap<String, Integer> Length_map=ChordLengthAnalysis(allChords_A,1); //1行の特徴やコードの長さを解析
        //----------↑Aメロ構成の特徴解析↑----------

        //----------↓Bメロ構成の特徴解析↓----------
        int new_line_n_B=0;
        if(!line_n_B.equals(""))new_line_n_B=Integer.parseInt(RandomChoice(UsedChord(line_n_B)));//new_line_n_Bは新しいコード進行の行数
        SparseArray<String[]> SA_matrix_B =ConstMatrix(allChords_B,new_line_n_B);
        String B_Const=RandomChoice(SumLineElement(SA_matrix_B,"B"));//sumを使って 行を比較してox!で行を分別 oxox oooo oxoo ox oo
        HashMap<String, List<String>> Connect_map_B =NextChordAnalysis(allChords_B);//コードの連結を調べる
        HashMap<String, Integer> Length_map_B=ChordLengthAnalysis(allChords_B,1); //1行の特徴やコードの長さを解析
        //----------↑Bメロ構成の特徴解析↑----------




        //----------↓Aメロ1行コード進行生成↓----------
        String[] fl_chord_A=GetNewFirstLastChord(allChords_A,"");//最初のコード決定
        String[] First_CP_A=FirstChordProgression(fl_chord_A[0],fl_chord_A[1], NC_list_map_A,Length_map,"");//最初の行のコード進行を決定
        new_chord_map_A.put(0,First_CP_A);//最初の行のコード進行をMAPに追加
        //----------↑Aメロ1行コード進行生成↑----------

        //----------↓Aメロ2行目以降コード進行生成↓----------
        String[] Position_Change_A=Position_Change_Block(CM_for_Random_A,new_line_n_A);//コード進行をコピペする際に変わる部分をランダムで生成し、パートの全行のぶんを作る
        String last_chord_A=First_CP_A[3];
        String[] sub_fl_chord_A=GetNewFirstLastChord(allChords_A,last_chord_A);//サブコードの最初のコードを決定
        HashMap<String, List<String>> Connect_map_sub_A =NextChordAnalysis(allChords_A);//コードの連結を調べる
        HashMap<String, Integer> Length_map_sub_A=ChordLengthAnalysis(allChords_A,1); //1行の特徴やコードの長さを解析
        String[] sub_CP_A=FirstChordProgression(sub_fl_chord_A[0],sub_fl_chord_A[1],Connect_map_sub_A,Length_map_sub_A,sub_fl_chord_A[0]);//最初の行のコード進行を調べる
        String[] str_A=A_Const.split("");//str[0]は空白 oxox oooo oxoo ox oo obob
        for(int i=1;i<str_A.length-1;i++){
            switch (str_A[i+1]){
                case "o":
                    new_chord_map_A.put(i,First_CP_A);
                    last_chord_A=First_CP_A[3];
                    //Log.i("テスト  ", "GetNewChords---o");
                    break;
                case "b":
                    String[] next_line_A=GetNextLine(Position_Change_A[i],First_CP_A,"A");
                    new_chord_map_A.put(i,next_line_A);
                    last_chord_A=next_line_A[3];
                    //Log.i("テスト  ", "GetNewChords---b");
                    break;
                case "x":
                    new_chord_map_A.put(i,sub_CP_A);//Bメロのコード進行を2行目に設定
                    last_chord_A=sub_CP_A[3];
                    //Log.i("テスト  ", "GetNewChords---x");
                    break;
                default:
            }

        }
        //----------↑Aメロ2行目以降コード進行生成↑----------




        //----------↓Bメロ1行コード進行生成↓----------
        String[] fl_chord_B=GetNewFirstLastChord(allChords_B,last_chord_A);//最初のコード決定
        String[] First_CP_B=FirstChordProgression(fl_chord_B[0],fl_chord_B[1], Connect_map_B,Length_map_B,"");//最初の行のコード進行を決定
        new_chord_map_B.put(0,First_CP_B);//最初の行のコード進行をMAPに追加
        //----------↑Bメロ1行コード進行生成↑----------

        //----------↓Bメロ2行目以降コード進行生成↓----------
        String[] Position_Change_B=Position_Change_Block(CM_for_Random_B,new_line_n_B);
        String last_chord_B=First_CP_B[3];
        String[] sub_fl_chord_B=GetNewFirstLastChord(allChords_B,last_chord_B);//サブコードの最初のコードを決定???????????????????????????
        HashMap<String, List<String>> Connect_map_sub_B =NextChordAnalysis(allChords_B);//コードの連結を調べる????????????????????????????????????????????
        HashMap<String, Integer> Length_map_sub_B=ChordLengthAnalysis(allChords_B,1); //1行の特徴やコードの長さを解析
        String[] sub_CP_B=FirstChordProgression(sub_fl_chord_B[0],sub_fl_chord_B[1], Connect_map_sub_B,Length_map_sub_B,fl_chord_B[0]);//最初の行のコード進行を調べる
        String[] str_B=B_Const.split("");//str_B[0]は空白 oxox oooo oxoo ox oo
        for(int i=1;i<str_B.length-1;i++){
            if(str_B[i+1].equals("o")){
                String[] next_line_B=GetNextLine(Position_Change_B[i],First_CP_B,"B");
                new_chord_map_B.put(i,next_line_B);
            }else if(str_B[i+1].equals("x")){
                new_chord_map_B.put(i,sub_CP_B);//Bメロのコード進行を2行目に設定
            }
        }
        //----------↑Bメロ2行目以降コード進行生成↑----------




        String UC_A_ALL=allChords_A.replaceAll("<!!>","").replaceAll("<->",",");
        String UC_B_ALL=allChords_B.replaceAll("<!!>","").replaceAll("<->",",");
        if(!line_n_A.equals(""))Data=Data+"\n---[Aメロ] 行数解析---\n"+UCtoString(UsedChord(line_n_A));
        if(!line_n_B.equals(""))Data=Data+"\n---[Bメロ] 行数解析---\n"+UCtoString(UsedChord(line_n_B));
        Data=Data+"\n---[Aメロ] Fコード解析---\n"+fl_chord_A[2]+"\n---[Bメロ] Fコード解析---\n"+fl_chord_B[2];
        Data=Data+"\n---[Aメロ] Lコード解析---\n"+fl_chord_A[3]+"\n---[Bメロ] Lコード解析---\n"+fl_chord_B[3];
        Data=Data+"\n---[Aメロ] コード出現率---\n"+UCtoString(UsedChordXXX(UC_A_ALL))+"\n---[Bメロ] コード出現率---\n"+UCtoString(UsedChordXXX(UC_B_ALL));


        String[] result = new String[5];
        result[0] = MapToString(new_chord_map_A);
        result[1] = MapToString(new_chord_map_B);
        result[2] =Data;
        result[3] =String.valueOf(new_line_n_A)+";"+String.valueOf(new_line_n_B);
        result[4] =fl_chord_A[0]+";"+fl_chord_B[0]+";"+fl_chord_A[1]+";"+fl_chord_B[1];
        return result;
    }





    //FirstChordProgressionで使う。１行のコード進行で、同じコードを使う場所に一気にコードを入れる。
    public String[] Check_FCP(int[] length_i,String[] new_chord_p,int i,String chord){

        for(int j=i+1;j<4;j++){
            if(length_i[i]==length_i[j]){
                new_chord_p[j]=chord;
                Log.i("テスト  ", "Check_FCP "+chord+"  "+j);
            }
        }
        return new_chord_p;
    }




    public String Chords_Adjust_Note(String chords){
        String result="";
        if(chords!=null) {
            chords=chords.replaceAll(",\n", "|\n");
            chords=chords.replaceAll(",", "||");
            String[] c_s = chords.split("\n", 0);
            for (String chord : c_s) {
                result = result + "|" + chord+"\n";
            }
            result=result.replaceAll("\\|\\[", "\\[");
        }
        return result;
    }




    //コードの長さや1行の特徴を解析
    public HashMap<String, Integer> ChordLengthAnalysis(String ALL_songs,int num) {
        int[] result = new int[4];
        HashMap<String, Integer> Length_map= new HashMap<>();
        String sum;
        num--;//num行目を解析する[num-1]
        if (ALL_songs.equals("")||num<0) return null;
        String[] song = ALL_songs.split("<!!>\n", 0);


        for(String chord:song) {
            int point = 0;
            result[0] = 10;
            result[1] = 10;
            result[2] = 10;
            result[3] = 10;
            if (chord.equals("")) continue;
            String[] c_s = chord.split("\\n", 0);
            if (c_s[num].equals("")) continue;
            String[] line_s = c_s[num].split(",", 0);
            if (line_s.length<4) continue;
            for (int i = 0; i < 4; i++) {
                if (result[i] > i) {
                    result[i] = point;
                    for (int j = i + 1; j < 4; j++) {
                        if (line_s[i].equals(line_s[j])) {
                            result[j] = point;
                        }
                    }
                    point++;
                }
            }

            if (result[3] == 10)continue;

            sum = String.valueOf(result[0]) +
                    String.valueOf(result[1]) +
                    String.valueOf(result[2]) +
                    String.valueOf(result[3]);

            if (Length_map.isEmpty()) {
                Length_map.put(sum, 1);
            } else {
                Integer n_times = Length_map.get(sum);
                if (n_times == null) {
                    Length_map.put(sum, 1);
                } else {
                    Length_map.put(sum, n_times + 1);
                }
            }


        }

        return Length_map;
    }









     //UCの出力をStringに
    public String UCtoString(HashMap<String, Integer> UC){
        String result="";



        if(!UC.isEmpty()) {

            //Map.Entry のリストを作る
            List<Entry<String, Integer>> entries = new ArrayList<>(UC.entrySet());
            //Comparator で Map.Entry の値を比較
            Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
                //比較関数
                @Override
                public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                    //return o1.getValue().compareTo(o2.getValue());    //昇順
                    return o2.getValue().compareTo(o1.getValue());    //降順
                }
            });
            for (Entry<String, Integer> e : entries) {
                result=result+e.getKey()+"   が   "+e.getValue()+"回\n";
            }


            // for (String key : UC.keySet()) {
            //     Integer n_times = UC.get(key);
            //     result=result+key+"   が   "+Integer.toString(n_times)+"回\n";
            // }
        }
        return result;
    }



    /*
    //構成を解析。行を比較して0123などの文字で表す　numは行数、line_nはフィルター。何行のものを調べるかの。
    public HashMap<String, Integer>LineAnalysis(String ALL_songs,int num,int line_n) {
        String[] box;
        HashMap<String, Integer> Line_map= new HashMap<>();
        num--;//1個目のコードはbox[0]に入るから
        if (ALL_songs.equals("")||num<0) return null;
        String[] song = ALL_songs.split("<!!>\n", 0);

        loop1:
        for(String chord:song) {
            if (chord.equals("")) continue;
            String[] split_c = chord.split("\\n", 0);
            if(split_c.length!=line_n && line_n!=0)continue;
            int[] result = new int[split_c.length];
            String[] line = new String[split_c.length];
            for (int i = 0; i < split_c.length; i++) {
                result[i] = 10;
                line[i] = "";
                if (!split_c[i].equals("")) {
                    box = split_c[i].split(",", 0);
                    if (box.length < num)continue loop1;
                    line[i] = box[num];
                }else continue loop1;

            }


            String sum = "";
            int point = 0;
            for (int i = 0; i < line.length; i++) {
                if (result[i] > i) {
                    result[i] = point;
                    for (int j = i + 1; j < line.length; j++) {
                        if (line[i].equals(line[j])) {
                            result[j] = point;
                        }

                    }
                    point++;
                }
            }

            for (int r_i : result) sum = sum + String.valueOf(r_i);

            if (Line_map.isEmpty()) {
                Line_map.put(sum, 1);
            } else {
                Integer n_times = Line_map.get(sum);
                if (n_times == null) {
                    Line_map.put(sum, 1);
                } else {
                    Line_map.put(sum, n_times + 1);
                }
            }
        }
        return Line_map;
    }
    */


    /*
    //UCの出力を入力し、ランダムで１つのコードを出力
    public String UCtoString(HashMap<String, Integer> UC){
        String result="";
        if(!UC.isEmpty()) {
            for (String key : UC.keySet()) {
                Integer n_times = UC.get(key);
                result=result+key+"が"+Integer.toString(n_times)+"回\n";
            }
        }
        return result;
    }
    */



    /*

    //構成を解析。行を比較して0123などの文字で表す
    public HashMap<String, Integer>LineAnalysis(String chord,HashMap<String, Integer> map,int num) {
        String[] box;
        if(num<1)return null;
        num--;//1個目のコードはbox[0]に入るから
        if (chord.equals("")) return map;
        String[] split_c = chord.split("\\n", 0);
        int[] result = new int[split_c.length];
        String[] line=new String[split_c.length];
        for (int i=0;i<split_c.length;i++){
            result[i]=10;
            line[i]="";
        }



        for (int i = 0; i < split_c.length; i++) {
            if(!split_c[i].equals("")){
                box = split_c[i].split(",", 0);
                if(box.length<num)return null;
                line[i]=box[num];
            }else return null;
        }


        String sum="";
        int point = 0;
        for (int i = 0; i < line.length; i++) {
            if(result[i]>i) {
                result[i]=point;
                for (int j = i + 1; j < line.length; j++) {
                    if (line[i].equals(line[j])) {
                        result[j] = point;
                    }

                }
                point++;
            }
        }

        for (int r_i : result)sum=sum+String.valueOf(r_i);

        if(map.isEmpty()) {
            map.put(sum, 1);
        }else{
            Integer n_times = map.get(sum);
            if(n_times==null){
                map.put(sum, 1);
            }else{
                map.put(sum, n_times+1);
            }
        }
        return map;
    }








    //対象の行とほかの行を比較し、同じコードの場合1点、違えば0点を加え、マップ生成する。
    public SparseArray<int[]> CompareLines(SparseArray<String[]> map,int target){

        if(map==null)return null;
        if(target>map.size())return null;
        int key=map.keyAt(target);
        String[] source=map.get(key);
        String[] target_str;
        int point[]=new int[4];
        SparseArray<int[]> result_map = new SparseArray<>();

        for(int i=0;i<map.size();i++){
            if(i==target-1)continue;
            key=map.keyAt(i);
            target_str=map.get(key);
            for(int j=0;j<4;j++){
                if(target_str[j].equals(source[j]))point[j]++;
            }
            result_map.put(i,point);
        }
        return result_map;
    }









    //最初、最後のコードがC(ルート)か調べ、点数出す。あんま意味ないかも
    public int FirstLastChord(String chords){
        int result=0;
        chords=Pattern.compile("\\n").matcher(chords).replaceAll("");
        chords=Pattern.compile("\\[.*?\\],").matcher(chords).replaceAll("");
        Pattern p_t,p_t2;
        p_t= Pattern.compile("^C.*");
        p_t2= Pattern.compile("^C#.*");
        if(chords!=null) {
            String[] data_split = chords.split(",", 0);
            Matcher m_t = p_t.matcher(data_split[0]);
            Matcher m_t2 = p_t2.matcher(data_split[0]);
            if (m_t.find()&&!(m_t2.find()))result= result + 1;

            //Matcher m_t3 = p_t.matcher(data_split[data_split.length-1]);
            //Matcher m_t4 = p_t2.matcher(data_split[data_split.length-1]);
            //if (m_t3.find()&&!(m_t4.find()))result= result + 1;
        }
        return result;
    }




    //コードの長さや1行の特徴を解析
    public HashMap<String, Integer> ChordLengthAnalysis2(String chord,HashMap<String, Integer> map) {
        int[] result = new int[4];
        String sum;
        int point = 0;
        result[0] = 10;
        result[1] = 10;
        result[2] = 10;
        result[3] = 10;
        if (chord.equals("")) return map;

        String[] line_s = chord.split(",", 0);

        for (int i = 0; i < 4; i++) {
            if(result[i]>i) {
                result[i]=point;
                for (int j = i + 1; j < 4; j++) {
                    if (line_s[i].equals(line_s[j])) {
                        result[j] = point;
                    }
                }
                point++;
            }
        }

        if(result[3]==10)return map;

        sum=    String.valueOf(result[0])+
                String.valueOf(result[1])+
                String.valueOf(result[2])+
                String.valueOf(result[3]);

        if(map.isEmpty()) {
            map.put(sum, 1);
        }else{
            Integer n_times = map.get(sum);
            if(n_times==null){
                map.put(sum, 1);
            }else{
                map.put(sum, n_times+1);
            }
        }
        return map;
    }




     */

}