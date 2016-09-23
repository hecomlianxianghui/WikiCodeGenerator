modelDotHFile(className, superClassName, properties, dotHHeaderComment) ::= <<
<dotHHeaderComment>

#import \<Foundation/Foundation.h>

/**
 *  
 */
@interface <className> : <superClassName>
<properties; separator="\n">

+ (<className> *)modleFromDic:(NSDictionary *)Dic;

@end
>>