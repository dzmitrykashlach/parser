package io.text

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import io.Parser


class TextParser(private val text: String) : Parser<String> {


    override fun tryParse(): String? {
        // Initialize the Stanford NLP pipeline
        val pipeline = StanfordCoreNLP(APPLICATION_PROPERTIES)

        // Perform sentiment analysis
        return getSentiment(text, pipeline)

    }

    private fun getSentiment(text: String, pipeline: StanfordCoreNLP): String {
        /* Create an Annotation object with the input text
        we're making annotation: key-value map - annotation type => text input */

        val annotation: Annotation = Annotation(text)

        /*
        Run all the NLP annotators on the text:
             TokenizerAnnotator
			 ParserAnnotator
			 SentimentAnnotator
         */
        pipeline.annotate(annotation)

        // Extract the sentiment from the annotation
        val sentence: CoreMap = annotation.get(CoreAnnotations.SentencesAnnotation::class.java).get(0)
        return sentence[SentimentCoreAnnotations.SentimentClass::class.java]
    }

    private companion object{
        val APPLICATION_PROPERTIES = "application.properties"
    }

}