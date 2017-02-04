/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Arrays;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordlist = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String, ArrayList<String>> letterstoword = new HashMap<String, ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizetoWords = new HashMap<Integer, ArrayList<String>>();


    private String sortLetters(String str)
    {
        char chars[] = str.toCharArray();
        Arrays.sort(chars);
        str = new String (chars);
        return str;
    }



    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordlist.add(word);
            wordSet.add(word);

            ArrayList<String> size_list = new ArrayList<String>();


            int size = word.length();

            if(! sizetoWords.containsKey(size)) {
                size_list.add(word);
                sizetoWords.put(size, size_list);
            }
            else
            {
                size_list=sizetoWords.get(size);
                size_list.add(word);
                sizetoWords.put(size,size_list) ;
            }

            String sorted_word = sortLetters(word);

            ArrayList<String> put_arr = new ArrayList<String>();

            if(! letterstoword.containsKey(sorted_word)) {
                put_arr.add(word);
                letterstoword.put(sorted_word, put_arr);
            }
            else
            {
                put_arr=letterstoword.get(sorted_word);
                put_arr.add(word);
                letterstoword.put(sorted_word , put_arr) ;
            }
        }
    }

    public boolean isGoodWord(String word, String base) {


        return wordSet.contains(word) && !word.contains(base);

    }

    public List<String> getAnagrams(String targetWord) {

        ArrayList<String> result = new ArrayList<String>();

        if (! wordSet.contains(targetWord))
            return result;

        String check_word = sortLetters(targetWord);

            for (int i =0; i < wordlist.size();i++)
            {
                String ss = sortLetters(wordlist.get(i));

                if( ss.equals(check_word))
                    if(ss.length() == check_word.length())
                        result.add(wordlist.get(i));
            }

        result.remove(targetWord);


        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        result.addAll(getAnagrams(word));

        for (char alpha='a';alpha<='z';alpha++)
        {
            String temp_word = word;
            temp_word += alpha;
            result.addAll(getAnagrams(temp_word));
        }
        return result;
    }

    public String pickGoodStarterWord() {


        ArrayList<String> Wordlist = new ArrayList<String>();

        Wordlist = sizetoWords.get(wordLength);

        int rand = random.nextInt(Wordlist.size() - 1);

        String str = "huli";

        for (;rand<Wordlist.size();rand++)
        {
            str = Wordlist.get(rand);

            List<String> nlist = new ArrayList<String>();

            nlist = getAnagramsWithOneMoreLetter(str);

            int num = nlist.size();


            if(num >= MIN_NUM_ANAGRAMS)
                break;

            if(rand == Wordlist.size()-1)
                rand=0;

            //break;
        }

        if(wordLength <= MAX_WORD_LENGTH)
            wordLength++;
        return str;
    }
}
