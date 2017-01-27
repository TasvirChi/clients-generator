// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Borhan Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Borhan Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
/*
 Forward declarations
 */
@class BorhanLibXmlWrapper;
@class BorhanXmlParserBase;
@class BorhanObjectBase;
@class BorhanException;

/*
 Protocol BorhanXmlParserDelegate
 */
@protocol BorhanLibXmlWrapperDelegate <NSObject>

@optional

- (void)parser:(BorhanLibXmlWrapper *)aParser didStartElement:(NSString *)aElementName;
- (void)parser:(BorhanLibXmlWrapper *)aParser didEndElement:(NSString *)aElementName;
- (void)parser:(BorhanLibXmlWrapper *)aParser foundCharacters:(NSString *)aString;
- (void)parser:(BorhanLibXmlWrapper *)aParser parseErrorOccurred:(NSError *)aParseError;

@end

/*
 Class BorhanLibXmlWrapper
 */
@interface BorhanLibXmlWrapper : NSObject
{
    struct _xmlParserCtxt* _xmlCtx;
    NSMutableString* _foundChars;
}

@property (nonatomic, assign) id<BorhanLibXmlWrapperDelegate> delegate;

- (void)processData:(NSData*)aData;
- (void)noMoreData;

@end

/*
 Protocol BorhanXmlParserDelegate
 */
@protocol BorhanXmlParserDelegate <NSObject>

- (void)parsingFinished:(BorhanXmlParserBase*)aParser;
- (void)parsingFailed:(BorhanXmlParserBase*)aParser;

@end

/*
 Class BorhanXmlParserBase
 */
@interface BorhanXmlParserBase : NSObject <BorhanLibXmlWrapperDelegate>
{
    id <BorhanLibXmlWrapperDelegate> _origDelegate;
    BOOL _attached;
}

@property (nonatomic, retain) BorhanLibXmlWrapper* parser;
@property (nonatomic, assign) id <BorhanXmlParserDelegate> delegate;
@property (nonatomic, retain) NSError* error;

- (void)attachToParser:(BorhanLibXmlWrapper*)aParser withDelegate:(id <BorhanXmlParserDelegate>)aDelegate;
- (void)detach;
- (void)callDelegateAndDetach;
- (void)parsingFailed:(BorhanXmlParserBase*)aParser;
- (id)result;

@end

/*
 Class BorhanXmlParserSkipTag
 */
@interface BorhanXmlParserSkipTag : BorhanXmlParserBase
{
    int _level;
}
@end

/*
 Class BorhanXmlParserSimpleType
 */
@interface BorhanXmlParserSimpleType : BorhanXmlParserBase
{
    NSString* _value;
}
@end

/*
 Class BorhanXmlParserException
 */
@interface BorhanXmlParserException : BorhanXmlParserBase <BorhanXmlParserDelegate>
{
    BorhanXmlParserBase* _subParser;
    BorhanXmlParserBase* _excObjParser;
    BorhanException* _targetException;
}

- (id)initWithSubParser:(BorhanXmlParserBase*)aSubParser;

@end

/*
 Class BorhanXmlParserObject
 */
@interface BorhanXmlParserObject : BorhanXmlParserBase <BorhanXmlParserDelegate>
{
    BorhanXmlParserBase* _subParser;
    BorhanObjectBase* _targetObj;
    NSString* _expectedType;
    NSString* _lastTagCapitalized;
    BOOL _lastIsObjectType;
    int _lastPropType;     // BorhanFieldType
}

- (id)initWithObject:(BorhanObjectBase*)aObject;
- (id)initWithExpectedType:(NSString*)aExpectedType;

@end

/*
 Class BorhanXmlParserArray
 */
@interface BorhanXmlParserArray : BorhanXmlParserBase <BorhanXmlParserDelegate>
{
    BorhanXmlParserBase* _subParser;
    NSString* _expectedType;
    NSMutableArray* _targetArr;
}

- (id)initWithExpectedType:(NSString*)aExpectedType;

@end

/*
 Class BorhanXmlParserMultirequest
 */
@interface BorhanXmlParserMultirequest : BorhanXmlParserBase <BorhanXmlParserDelegate>
{
    NSMutableArray* _subParsers;
    int _reqIndex;
}

- (void)addSubParser:(BorhanXmlParserBase*)aParser;
- (int)reqCount;

@end

/*
 Class BorhanXmlParserSkipPath
 */
@interface BorhanXmlParserSkipPath : BorhanXmlParserBase <BorhanXmlParserDelegate>
{
    BorhanXmlParserBase* _subParser;
    NSArray* _path;
    int _pathPosition;
    int _skipLevel;
}

- (id)initWithSubParser:(BorhanXmlParserBase*)aSubParser withPath:(NSArray*)aPath;

@end
